package cl.duoc.band_world.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoDisturb
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.band_world.data.Names
import cl.duoc.band_world.functions.BandFunctions
import kotlin.collections.forEach


enum class FilterType { BANDS, ARTISTS }

enum class FilterState { NONE, INCLUDE, EXCLUDE }

enum class Filters(
    val filterType: FilterType,
//    val options: List<Names>,
    val langEs: String,
) {
    GENRE(filterType = FilterType.BANDS, "Género"),
    COUNTRY(filterType = FilterType.BANDS, "País"),
    STATUS(filterType = FilterType.BANDS, "Estado"),
}

data class ListaFiltro(
    val filter: Filters,
    val selected: List<String>
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterButton(
    bandFunctions: BandFunctions,
    filterOption: FilterType,
    includes: List<ListaFiltro>,
    excludes: List<ListaFiltro>,
    onChange: (List<ListaFiltro>, List<ListaFiltro>) -> Unit
) {

    var mostrarFiltros by remember {
        mutableStateOf(false)
    }

    IconButton(
        onClick = {
            mostrarFiltros = true
        }
    ) {
        Icon(
            imageVector = Icons.Default.FilterList,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
    }

    if (mostrarFiltros) {
        ModalBottomSheet(
            onDismissRequest = {
                mostrarFiltros = false
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Text(
                    text = "Filtros",
                    style = MaterialTheme.typography.headlineSmall
                )

                addHeight()

                Filters.entries
                    .filter { it.filterType == filterOption }
                    .forEach { filter ->
                        FilterThreeState(
                            bandFunctions = bandFunctions,
                            filter = filter,
                            includes = includes,
                            excludes = excludes,
                            onChange = onChange
                        )
                    }

                addHeight()
            }
        }
    }
}


@Composable
fun FilterThreeState(
    bandFunctions: BandFunctions,
    filter: Filters,
    includes: List<ListaFiltro>,
    excludes: List<ListaFiltro>,
    onChange: (List<ListaFiltro>, List<ListaFiltro>) -> Unit
) {

    var genreNames by remember { mutableStateOf<List<Names>>(emptyList()) }
    var countryNames by remember { mutableStateOf<List<Names>>(emptyList()) }
    var stateNames by remember { mutableStateOf<List<Names>>(emptyList()) }

    LaunchedEffect(Unit) {
        genreNames = bandFunctions.getGenreNames()
        countryNames = bandFunctions.getCountryNames()
        stateNames = bandFunctions.getStatusNames()
    }

    val options = when (filter) {
        Filters.GENRE -> genreNames
        Filters.COUNTRY -> countryNames
        Filters.STATUS -> stateNames
    }

    Text(
        text = filter.langEs,
        style = MaterialTheme.typography.titleMedium
    )

    addHeight(8.dp)

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 100.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // foreach
        options.forEach { opcion ->

            val include = includes.find { it.filter == filter }?.selected?.contains(opcion.id) == true
            val exclude = excludes.find { it.filter == filter }?.selected?.contains(opcion.id) == true

            val currentState = when {
                include -> FilterState.INCLUDE
                exclude -> FilterState.EXCLUDE
                else -> FilterState.NONE
            }


            FilterChip(
                selected = currentState != FilterState.NONE,
                onClick = {
                    val newIncludes = includes.toMutableList()
                    val newExcludes = excludes.toMutableList()

                    val filtroInclEncontrado = newIncludes.find { it.filter == filter }
                    val filtroExclEncontrado = newExcludes.find { it.filter == filter }


                    when (currentState) {

                        // neutro -> include
                        FilterState.NONE -> {
                            if(filtroInclEncontrado != null) {
                                newIncludes.remove(filtroInclEncontrado)
                                newIncludes.add(filtroInclEncontrado.copy(selected = filtroInclEncontrado.selected + opcion.id))
                            } else {
                                newIncludes.add(ListaFiltro(filter = filter, selected = listOf(opcion.id)))
                            }
                        }


                        // include -> exclude
                        FilterState.INCLUDE -> {
                            if(filtroInclEncontrado != null) {
                                newIncludes.remove(filtroInclEncontrado)

                                val nuevosSelected = filtroInclEncontrado.selected - opcion.id
                                if(nuevosSelected.isNotEmpty()) {
                                    newIncludes.add(filtroInclEncontrado.copy(selected = nuevosSelected))
                                }

                            }

                            if(filtroExclEncontrado != null) {
                                newExcludes.remove(filtroExclEncontrado)
                                newExcludes.add(filtroExclEncontrado.copy(selected = filtroExclEncontrado.selected + opcion.id))
                            } else {
                                newExcludes.add(ListaFiltro(filter = filter, selected = listOf(opcion.id)))
                            }
                        }


                        // exclude -> neutro
                        FilterState.EXCLUDE -> {
                            if(filtroExclEncontrado != null) {
                                newExcludes.remove(filtroExclEncontrado)

                                val nuevosSelected = filtroExclEncontrado.selected - opcion.id
                                if(nuevosSelected.isNotEmpty()) {
                                    newExcludes.add(filtroExclEncontrado.copy(selected = nuevosSelected))
                                }
                            }
                        }
                    }

                    onChange(newIncludes, newExcludes)
                },
                label = {
                    Text(text = opcion.name)
                },
                leadingIcon = {
                    val imageVector = when (currentState) {
                        FilterState.NONE -> null
                        FilterState.INCLUDE -> Icons.Filled.Done
                        FilterState.EXCLUDE -> Icons.Filled.DoDisturb
                    }
                    if(imageVector != null) {
                        Icon(
                            imageVector = imageVector,
                            contentDescription = "Filtro aplicado",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                }
            )
        }
    }

}

