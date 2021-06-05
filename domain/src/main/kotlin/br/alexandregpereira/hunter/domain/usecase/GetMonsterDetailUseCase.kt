/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.domain.usecase

import br.alexandregpereira.hunter.domain.model.MeasurementUnit
import br.alexandregpereira.hunter.domain.model.Monster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip

typealias MonsterDetail = Triple<Int, List<Monster>, MeasurementUnit>

class GetMonsterDetailUseCase internal constructor(
    private val getMeasurementUnitUseCase: GetMeasurementUnitUseCase,
    private val getMonstersUseCase: GetMonstersUseCase,
) {

    operator fun invoke(index: String): Flow<MonsterDetail> {
        return getMonstersUseCase().zip(getMeasurementUnitUseCase()) { monsters, measurementUnit ->
            val monster = monsters.find { monster -> monster.index == index }
                ?: throw IllegalAccessError("Monster not found")

             Triple(monsters.indexOf(monster), monsters, measurementUnit)
        }
    }
}
