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

package br.alexandregpereira.hunter.detail.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.detail.R
import br.alexandregpereira.hunter.domain.model.Action
import br.alexandregpereira.hunter.domain.model.DamageDice

@Composable
fun ActionBlock(
    actions: List<Action>,
    modifier: Modifier = Modifier
) = AbilityDescriptionBlock(
    title = stringResource(R.string.monster_detail_actions),
    abilityDescriptions = actions.map { it.abilityDescription },
    modifier = modifier
) { index ->

    val attackBonus = actions[index].attackBonus
    val damageDices = actions[index].damageDices
    if (attackBonus == null && damageDices.isEmpty()) return@AbilityDescriptionBlock

    ActionDamageGrid(
        attackBonus = actions[index].attackBonus,
        damageDices = actions[index].damageDices,
        modifier = Modifier.padding(top = 16.dp)
    )
}

@Composable
fun ActionDamageGrid(
    attackBonus: Int?,
    damageDices: List<DamageDice>,
    modifier: Modifier = Modifier
) = Grid(modifier = modifier) {

    val iconSize = 48.dp
    attackBonus?.let {
        Bonus(value = it, name = stringResource(R.string.monster_detail_attack), iconSize = iconSize)
    }

    damageDices.forEach { damageDice ->
        val iconRes = damageDice.damage.type.getIconRes()
        if (iconRes != null) {
            IconInfo(
                title = damageDice.dice,
                painter = painterResource(iconRes),
                iconColor = damageDice.damage.type.getIconColor(),
                iconAlpha = 1f,
                iconSize = iconSize
            )
        }
    }
}
