package com.chatgpt.letaithink.utils

import com.chatgpt.letaithink.data.AiImage
import com.chatgpt.letaithink.data.AiMessage
import com.chatgpt.letaithink.data.ConversationItem
import com.chatgpt.letaithink.data.UserMessage
import com.chatgpt.letaithink.model.remoteModelts.ImageModel
import com.chatgpt.letaithink.model.TextCompletion
import com.chatgpt.letaithink.model.databaseModels.MessageEntity
import com.chatgpt.letaithink.model.databaseModels.MessageType
import com.google.gson.JsonSyntaxException
import java.util.*

// TODO Fix exception cases in usages
@Throws(IllegalStateException::class)
fun ConversationItem.toMessageEntity(sessionId: String): MessageEntity {
    val currentTime = Calendar.getInstance().timeInMillis
    return when (this) {
        is UserMessage -> MessageEntity(sessionId, MessageType.USER_INPUT, message, currentTime)
        is AiMessage -> MessageEntity(sessionId, MessageType.AI_COMPLETION, JsonUtil.toJson(textCompletion), currentTime)
        is AiImage -> MessageEntity(sessionId, MessageType.AI_IMAGE_GENERATION, JsonUtil.toJson(image), currentTime)
        else -> throw IllegalStateException("Unknown conversation item: ${javaClass.simpleName}")
    }
}

@Throws(JsonSyntaxException::class)
fun MessageEntity.toConversationItem(): ConversationItem {
    val conversationItem = when (type) {
        MessageType.USER_INPUT -> UserMessage(content)
        MessageType.AI_COMPLETION -> AiMessage(JsonUtil.fromJson(content, TextCompletion::class.java))
        MessageType.AI_IMAGE_GENERATION -> AiImage(JsonUtil.fromJson(content, ImageModel::class.java))
    }

    return conversationItem
}