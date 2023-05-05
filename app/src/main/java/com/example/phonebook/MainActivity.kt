package com.example.phonebook

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.phonebook.routing.MyPhoneRouter
import com.example.phonebook.routing.Screen
import com.example.phonebook.screens.PhoneScreen
import com.example.phonebook.screens.SavePhoneScreen
import com.example.phonebook.screens.TrashScreen
import com.example.phonebook.ui.theme.MyPhoneTheme
import com.example.phonebook.viewModel.MainViewModel
import com.example.phonebook.viewModel.MainViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPhoneTheme() {
                val viewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(LocalContext.current.applicationContext as Application)
                )
                // A surface container using the 'background' color from the theme

                    MainActivityScreen(viewModel)

            }
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainActivityScreen(viewModel: MainViewModel) {
    Surface {
        when (MyPhoneRouter.currentScreen) {
            is Screen.Phones -> PhoneScreen(viewModel)
            is Screen.SavePhone -> SavePhoneScreen(viewModel)
            is Screen.Trash -> TrashScreen(viewModel)
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyPhoneTheme() {
        Greeting("Android")
    }
}