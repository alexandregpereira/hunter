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

package br.alexandregpereira.hunter.data.local.mapper

import br.alexandregpereira.hunter.data.local.entity.DamageEntity
import br.alexandregpereira.hunter.data.local.entity.DamageImmunityEntity
import br.alexandregpereira.hunter.data.local.entity.DamageResistanceEntity
import br.alexandregpereira.hunter.data.local.entity.DamageVulnerabilityEntity
import br.alexandregpereira.hunter.data.local.entity.ValueEntity
import br.alexandregpereira.hunter.domain.model.Damage
import br.alexandregpereira.hunter.domain.model.DamageType

internal fun List<DamageEntity>.toDamageDomain(): List<Damage> {
    return this.map {
        it.toDamageDomain()
    }
}

internal fun DamageEntity.toDamageDomain(): Damage {
    return Damage(
        index = this.value.index,
        type = DamageType.valueOf(this.value.type),
        name = this.value.name
    )
}

internal fun ValueEntity.toDamageDomain(): Damage {
    return Damage(
        index = this.index,
        type = DamageType.valueOf(this.type),
        name = this.name
    )
}

internal fun List<Damage>.toDamageResistanceEntity(
    monsterIndex: String
): List<DamageResistanceEntity> {
    return this.map {
        it.toDamageResistanceEntity(monsterIndex)
    }
}

internal fun List<Damage>.toDamageImmunityEntity(monsterIndex: String): List<DamageImmunityEntity> {
    return this.map {
        it.toDamageImmunityEntity(monsterIndex)
    }
}

internal fun List<Damage>.toDamageVulnerabilityEntity(
    monsterIndex: String
): List<DamageVulnerabilityEntity> {
    return this.map {
        it.toDamageVulnerabilityEntity(monsterIndex)
    }
}

internal fun Damage.toDamageResistanceEntity(monsterIndex: String): DamageResistanceEntity {
    return DamageResistanceEntity(
        value = ValueEntity(
            index = this.index,
            type = this.type.name,
            name = this.name,
            monsterIndex = monsterIndex
        )
    )
}

internal fun Damage.toDamageImmunityEntity(monsterIndex: String): DamageImmunityEntity {
    return DamageImmunityEntity(
        value = ValueEntity(
            index = this.index,
            type = this.type.name,
            name = this.name,
            monsterIndex = monsterIndex
        )
    )
}

internal fun Damage.toDamageVulnerabilityEntity(monsterIndex: String): DamageVulnerabilityEntity {
    return DamageVulnerabilityEntity(
        value = ValueEntity(
            index = this.index,
            type = this.type.name,
            name = this.name,
            monsterIndex = monsterIndex
        )
    )
}

internal fun Damage.toEntity(monsterIndex: String): ValueEntity {
    return ValueEntity(
        index = this.index,
        type = this.type.name,
        name = this.name,
        monsterIndex = monsterIndex
    )
}
