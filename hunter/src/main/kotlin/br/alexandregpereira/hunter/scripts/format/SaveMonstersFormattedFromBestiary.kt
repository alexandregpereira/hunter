/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.scripts.format

import br.alexandregpereira.hunter.bestiary.Monster
import br.alexandregpereira.hunter.bestiary.getMonstersFromBestiary
import br.alexandregpereira.hunter.data.remote.model.MonsterDto
import br.alexandregpereira.hunter.data.remote.model.MonsterSizeDto
import br.alexandregpereira.hunter.data.remote.model.MonsterTypeDto
import br.alexandregpereira.hunter.data.remote.model.SourceDto
import br.alexandregpereira.hunter.data.remote.model.SpeedDto
import br.alexandregpereira.hunter.data.remote.model.SpeedTypeDto
import br.alexandregpereira.hunter.scripts.MONSTER_JSON_FILE_NAME
import br.alexandregpereira.hunter.scripts.saveJsonFile
import br.alexandregpereira.hunter.scripts.start
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList
import java.util.Locale

@FlowPreview
@ExperimentalCoroutinesApi
suspend fun main() = start {
    getMonstersFromBestiary()
        .map { monsters ->
            monsters.filter { it.cr != null && it.hp.average != null }
                .asMonstersFormatted()
        }
        .single()
        .asSequence()
        .asFlow()
//        .flatMapMerge {
//            it.downloadImage()
//        }
        .toList()
        .filterNotNull()
        .sortedBy { it.name }
        .groupBy { it.source.acronym }
        .let {
            it.forEach { entry ->
                val monsters = entry.value
                println("\n${monsters.size} monsters formatted")
                monsters.forEach { monster ->
                    println("id: ${monster.index}, name: ${monster.name}")
                }

                val fileName =
                    "json/$MONSTER_JSON_FILE_NAME-${entry.key.toLowerCase(Locale.ROOT)}.json"
                saveJsonFile(monsters, fileName, printJson = false)
                return@start
            }
        }
}

private fun List<Monster>.asMonstersFormatted(): List<MonsterDto> {
    return this.mapNotNull {
        runCatching {
            MonsterDto(
                index = it.getIndex(),
                source = it.sourceFormatted(),
                type = it.type.typeFormatted(),
                subtype = null,
                group = it.getGroup(),
                challengeRating = it.cr!!.challengeRatingFormatted(),
                name = it.name,
                subtitle = "",
                imageUrl = getImageUrl(it.getIndex()),
                isHorizontalImage = false,
                size = MonsterSizeDto.valueOf(it.size.name),
                alignment = it.alignmentFormatted(),
                armorClass = it.ac,
                hitPoints = it.hp.average!!,
                hitDice = it.hp.formula!!.replace(" ", ""),
                speed = it.speedFormatted(),
                abilityScores = listOf(),
                savingThrows = listOf(),
                skills = listOf(),
                damageVulnerabilities = listOf(),
                damageResistances = listOf(),
                damageImmunities = listOf(),
                conditionImmunities = listOf(),
                senses = listOf(),
                languages = "",
                specialAbilities = listOf(),
                actions = listOf()
            )
        }.getOrElse { error ->
            print("Monster error: ${it.name}")
            error.printStackTrace()
            println()
            null
        }
    }
}

private fun Monster.getIndex(): String {
    return name.replace(" ", "-").toLowerCase(Locale.ROOT)
}

private fun String.typeFormatted(): MonsterTypeDto {
    return runCatching {
        MonsterTypeDto.valueOf(this.toLowerCase(Locale.ROOT))
    }.getOrDefault(MonsterTypeDto.HUMANOID)
}

private fun Monster.getGroup(): String? {
    val index = getIndex()
    return when {
        isGroupByIndex(index) -> {
            getGroupByIndex(index)
        }
        else -> {
            getGroupByGroupMap(index)
        }
    }
}

private fun String.challengeRatingFormatted(): Float {
    return if (this.contains("/").not()) this.toFloat() else {
        val numbers = this.split("/")
        numbers[0].toFloat() / numbers[1].toFloat()
    }
}

private fun Monster.alignmentFormatted(): String {
    return alignment.joinToString("-")
        .replace("NX-C-G-NY-E", "L-NX-C-G-NY-E")
        .replace("L-NX-C-NY-E", "L-NX-C-G-NY-E")
        .replace("A", "U")
        .run {
            when (this) {
                "A" -> "any alignment"
                "C-E" -> "chaotic evil"
                "C-G" -> "chaotic good"
                "C-G-NY-E" -> "chaotic good or evil"
                "C-N" -> "chaotic neutral"
                "L" -> "any lawful alignment"
                "L-E" -> "lawful evil"
                "L-G" -> "lawful good"
                "L-N" -> "lawful neutral"
                "L-NX-C-E" -> "lawful or chaotic evil"
                "L-NX-C-G-NY-E" -> "lawful or chaotic good or evil"
                "N" -> "neutral"
                "N-E" -> "neutral evil"
                "N-G" -> "neutral good"
                else -> "unaligned"
            }
        }
}

private fun Monster.sourceFormatted(): SourceDto {
    return SourceDto(
        name = sourceName,
        acronym = source
    )
}

private fun Monster.speedFormatted(): SpeedDto {
    val burrow = createSpeedValue(SpeedTypeDto.BURROW, speed.burrow.toString())
    val climb = createSpeedValue(SpeedTypeDto.CLIMB, speed.climb.toString())
    val fly = createSpeedValue(SpeedTypeDto.FLY, speed.fly.toString())
    val walk = createSpeedValue(SpeedTypeDto.WALK, speed.walk.toString())
    val swim = createSpeedValue(SpeedTypeDto.SWIM, speed.swim.toString())

    return SpeedDto(
        hover = speed.canHover,
        values = listOfNotNull(burrow, climb, fly, walk, swim)
    )
}