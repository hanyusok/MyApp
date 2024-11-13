package com.cafe24

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import com.cafe24.ui.theme.SupabaseAppTheme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

val supabase = createSupabaseClient(
    supabaseUrl = "http://martclinic.cafe24.com:8000",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.ewogICJyb2xlIjogImFub24iLAogICJpc3MiOiAic3VwYWJhc2UiLAogICJpYXQiOiAxNzMwOTkxNjAwLAogICJleHAiOiAxODg4NzU4MDAwCn0.lA6CORXNZ8FLfK3_Y0dVo7XgavbtrdOfNZh1ursbjQQ"
) {
    install(Postgrest)
}

@Serializable
data class Clinic (
    @SerialName("id")
    val id: Int,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("province")
    val province: String,
    @SerialName("city")
    val city: String,
    @SerialName("clinic_name")
    val clinicName: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    @SerialName("category")
    val category: String,
    @SerialName("rat_available")
    val ratAvailable: Boolean,
    @SerialName("address")
    val address: String,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            SupabaseAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    ClinicsList()
                }

            }
        }
    }
}

@Composable
fun ClinicsList(){
    var clinics by remember {
        mutableStateOf<List<Clinic>>(listOf())
    }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO){
            clinics = supabase.from("clinics").select().decodeList<Clinic>()
        }
    }
    LazyColumn {
        items(clinics, key = { clinic -> clinic.clinicName}){
            clinic -> Text(clinic.clinicName, modifier = Modifier.padding(8.dp))
        }
    }
}
