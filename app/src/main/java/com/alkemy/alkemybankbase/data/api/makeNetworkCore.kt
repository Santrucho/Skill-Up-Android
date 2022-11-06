package com.alkemy.alkemybankbase.data.api

 import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
 import retrofit2.HttpException
 import java.net.UnknownHostException

suspend fun <T> makeNetworkCall(
    call: suspend() -> T
):apiResponsesStatus<T> = withContext(Dispatchers.IO){
        try {
            apiResponsesStatus.SUCCESS(call())
        }
        catch (e:UnknownHostException){
            apiResponsesStatus.ERROR("Error descargando los datos")
        }
        catch (e:HttpException){
            val errorMessage =
            if(e.code()== 400){
                "Usuario incorrecto o contraseña"

            }else{
                "Error desconocido"
            }
        apiResponsesStatus.ERROR(errorMessage)
        }

        catch (e:Exception){

          val errorMessage=   when(e.message){

                "sign_up_error" ->  "Error al registrarse"
                "sign_in_error" ->  "Error al entrar"
                "user_already_exists" ->  "Usuario ya existe"

                else -> "Error desconocido"
            }

            apiResponsesStatus.ERROR(errorMessage)
        }
    }

