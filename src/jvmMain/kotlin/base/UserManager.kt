package base

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import model.LoginResult
import util.DataStoreUtils

object UserManager {

    const val KEY_LOCAL_LOGIN_RESULT = "KEY_LOCAL_LOGIN_RESULT"

    fun getLoginResultFlow(): Flow<LoginResult?> {
        return DataStoreUtils.getData(KEY_LOCAL_LOGIN_RESULT, "")
            .map {

                var loginResult: LoginResult? = null
                try {
                    loginResult = Gson().fromJson(it, LoginResult::class.java)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                loginResult
            }
    }

    fun getLoginResult(): LoginResult?  {
        return try {
            val json = DataStoreUtils.readStringData(KEY_LOCAL_LOGIN_RESULT, "")
            Gson().fromJson(json, LoginResult::class.java)
        } catch (e: Exception) {
            null
        }
    }


    suspend fun saveLoginResult(json: String) {
        DataStoreUtils.putData(KEY_LOCAL_LOGIN_RESULT, json)
    }

}
