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

package br.alexandregpereira.hunter.data.preferences

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class PreferencesDataSourceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : PreferencesDataSource {

    override fun getInt(key: String, defaultValue: Int): Flow<Int> {
        return flow {
            emit(sharedPreferences.getInt(key, defaultValue))
        }
    }

    override fun getString(key: String, defaultValue: String): Flow<String> {
        return flow {
            emit(sharedPreferences.getString(key, defaultValue) ?: defaultValue)
        }
    }

    override fun save(key: String, value: Any): Flow<Unit> {
        return flow {
            val editor = sharedPreferences.edit()
            when (value) {
                is Int -> editor.putInt(key, value).apply()
                is String -> editor.putString(key, value).apply()
                else -> throw UnsupportedOperationException("${value::class} not supported")
            }
            emit(Unit)
        }
    }
}
