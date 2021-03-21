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

package br.alexandregpereira.hunter.app.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.alexandregpereira.hunter.monster.compendium.MonsterCompendiumViewModel
import br.alexandregpereira.hunter.monster.compendium.MonsterCompendiumViewState
import br.alexandregpereira.hunter.monster.compendium.ui.MonsterCard
import br.alexandregpereira.hunter.ui.theme.HunterTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {

    private val viewModel: MonsterCompendiumViewModel by viewModel()

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadMonsters()
        setContent {
            val state = viewModel.stateLiveData.observeAsState().value ?: return@setContent
            Window(state)
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun Window(state: MonsterCompendiumViewState) = HunterTheme {
    Surface {
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(state.monsters) {
                MonsterCard(
                    imageUrl = it.imageUrl,
                    backgroundColor = it.backgroundColor,
                    contentDescription = it.name,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Preview
@Composable
fun DefaultPreview() {
    Window(MonsterCompendiumViewState())
}