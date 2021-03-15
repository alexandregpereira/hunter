package br.alexandregpereira.beholder.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AbilityScore(
    @SerialName("type")
    val type: AbilityScoreType,
    @SerialName("value")
    val value: Int,
    @SerialName("modifier")
    val modifier: Int
)

@Serializable
data class SavingThrow(
    @SerialName("type")
    val type: AbilityScoreType,
    @SerialName("modifier")
    val modifier: Int
)

@Serializable
data class Skill(
    @SerialName("id")
    val id: String,
    @SerialName("modifier")
    val modifier: Int
)

@Serializable
enum class AbilityScoreType {
    @SerialName("STRENGTH")
    STRENGTH,
    @SerialName("DEXTERITY")
    DEXTERITY,
    @SerialName("CONSTITUTION")
    CONSTITUTION,
    @SerialName("INTELLIGENCE")
    INTELLIGENCE,
    @SerialName("WISDOM")
    WISDOM,
    @SerialName("CHARISMA")
    CHARISMA
}