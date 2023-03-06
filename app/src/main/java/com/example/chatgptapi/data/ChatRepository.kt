package com.example.chatgptapi.data

import com.example.chatgptapi.model.*
import com.example.chatgptapi.model.databaseModels.MessageEntity
import com.example.chatgptapi.model.databaseModels.MessageType
import com.example.chatgptapi.model.databaseModels.SessionEntity
import com.example.chatgptapi.model.remoteModelts.CompletionRequest
import com.example.chatgptapi.utils.JsonUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*

// TODO add suspend keyword for methods which should run on IO thread
object ChatRepository {

    private val remoteDataSource = RemoteDataSource()
    private val localDataSource = LocalDataSource()

    val chatModes: List<ChatMode> = localDataSource.chatModes

    suspend fun askQuestion(completion: CompletionRequest): TextCompletion = withContext(Dispatchers.IO) {
        remoteDataSource.getCompletion(completion)!! // TODO unsafe call
    }

    suspend fun generateImage(imageParams: ImageGenerationRequest) = withContext(Dispatchers.IO) {
        remoteDataSource.generateImage(imageParams)
    }

    suspend fun createSession(name: String): SessionEntity {
        val session = SessionEntity(UUID.randomUUID().toString(), name, UserRepository.getUser()?.userId!!)
        localDataSource.insertSession(session)
        return session
    }

    suspend fun saveConversationItem(session: SessionEntity, questionItem: ConversationItem, answerItem: ConversationItem) {
        val question = questionItem.toMessageEntity(session.sessionId)
        val answer = answerItem.toMessageEntity(session.sessionId)
        localDataSource.insertConversationItem(question, answer)
    }

    @Throws(IllegalStateException::class)
    private fun ConversationItem.toMessageEntity(sessionId: String): MessageEntity {
        val currentTime = Calendar.getInstance().timeInMillis
        return when (this) {
            is UserMessage -> MessageEntity(sessionId, MessageType.USER_INPUT, message, currentTime)
            is AiMessage -> MessageEntity(sessionId, MessageType.AI_COMPLETION, JsonUtil.toJson(textCompletion), currentTime)
            is AiImage -> MessageEntity(sessionId, MessageType.AI_IMAGE_GENERATION, JsonUtil.toJson(image), currentTime)
            else -> throw IllegalStateException("Unknown conversation item: ${javaClass.simpleName}")
        }
    }

    fun getChatSessions(): Flow<List<SessionEntity>> = localDataSource.getAllSessions()

    suspend fun getChatSession(id: String): SessionEntity? = localDataSource.getChatSession(id)

    suspend fun deleteSession(session: SessionEntity) {
        localDataSource.deleteChatSession(session)
    }

    suspend fun updateSessionName(session: SessionEntity, name: String) {
        localDataSource.updateSessionName(session, name)
    }
}