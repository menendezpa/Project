package com.project.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.project.data.Task
import com.project.ui.component.AppBottomBar
import com.project.ui.component.AppTopBar
import com.project.ui.component.CalendarGrid
import com.project.ui.component.CalendarHeader
import com.project.ui.component.FAB
import com.project.ui.component.TaskList
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel()
) {

    Home(navController = navController, viewModel = viewModel/*, tasks = tasks*/)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(
    viewModel: HomeViewModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadTasksForUser()
            }
        }

        val lifecycle = lifecycleOwner.lifecycle
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }

    // Estados observables
    val tasks by viewModel.tasks.collectAsState()
    val daysWithTasks by viewModel.daysWithTasks.collectAsState()

    Scaffold(
        topBar = { AppTopBar(canNavigateBack = false) },
        floatingActionButton = {
            FAB(
                onFirstClick = { viewModel.logOut()
                    navController.navigate("login"){
                        popUpTo("home"){inclusive = true}
                    } },
                onThirdClick = {
                    navController.navigate("GymScreen")
                }
            )
        }
    ) { innerPadding ->
        Column {
            Card(
                modifier = modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CalendarHeader(
                        currentMonth = currentMonth,
                        onPreviousMonth = { currentMonth = currentMonth.minusMonths(1) },
                        onNextMonth = { currentMonth = currentMonth.plusMonths(1) }
                    )
                    CalendarGrid(
                        yearMonth = currentMonth,
                        onDayClick = { selectedDate = it },
                        daysWithTask = daysWithTasks
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                thickness = 3.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
            )

            selectedDate?.let { date ->
                TaskList(
                    tasks = tasks,
                    onTaskClick = {},
                    date = date
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(modifier: Modifier = Modifier) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    // En un ejemplo real, estas tareas vendrían de un ViewModel o de otra fuente
    val tasks = listOf(
        Task(
            id = "1",
            taskName = "Task 1",
            description = "Description 1",
            date = LocalDate.of(2025, 5, 1).toString()
        ),
        Task(
            id = "2",
            taskName = "Task 2",
            description = "Description 2",
            date = LocalDate.of(2025, 5, 5).toString()
        ),
        Task(
            id = "3",
            taskName = "Task 3",
            description = "Description 3",
            date = LocalDate.of(2025, 5, 10).toString()
        ),
        Task(
            id = "4",
            taskName = "Task 4",
            description = "Description 4",
            date = LocalDate.of(2025, 5, 15).toString()
        ),
        Task(
            id = "5",
            taskName = "Task 5",
            description = "Description 5",
            date = LocalDate.of(2025, 5, 20).toString()
        )
    )

    val daysWithTasks = setOf(
        LocalDate.of(2025, 5, 1),
        LocalDate.of(2025, 5, 5),
        LocalDate.of(2025, 5, 10)
    )

    Scaffold(
        topBar = { AppTopBar(canNavigateBack = false) },
        bottomBar = { AppTopBar(canNavigateBack = false) },
        floatingActionButton = { FAB() },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.primary,
    ) { innerPadding ->
        Row {
            Card(
                modifier = modifier
                    .padding(innerPadding)
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CalendarHeader(
                        currentMonth = currentMonth,
                        onPreviousMonth = { currentMonth = currentMonth.minusMonths(1) },
                        onNextMonth = { currentMonth = currentMonth.plusMonths(1) }
                    )

                    CalendarGrid(
                        yearMonth = currentMonth,
                        onDayClick = { selectedDate = it },
                        daysWithTask = daysWithTasks
                    )


                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    thickness = 3.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
            }
            selectedDate?.let { date ->
                // Ahora, al cambiar la fecha, filteredTasks se recalcula y se renderiza la lista correspondiente.
                TaskList(tasks = tasks, onTaskClick = {}, date = date)
            }
        }
    }
}

@Preview(showBackground = true)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreenPreview() {
    Home()
}
