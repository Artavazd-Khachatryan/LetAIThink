package com.openai.api.models

import com.google.gson.annotations.SerializedName

data class AiModel(
    var id: String?,
    @SerializedName("object")
    var model: String?,
    @SerializedName("organization-owner")
    var organization: String?,
    @SerializedName("permission")
    var permissions : List<Any>?
)
