package com.matiasilveiro.automastichome.main.framework

import android.util.Log
import com.google.gson.Gson
import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.main.data.NodesDataSource
import com.matiasilveiro.automastichome.main.domain.CentralNode
import com.matiasilveiro.automastichome.main.domain.ControlFeedback
import com.matiasilveiro.automastichome.main.domain.RemoteActuator
import com.matiasilveiro.automastichome.main.domain.RemoteSensor
import com.matiasilveiro.automastichome.main.framework.mappers.toCentralNode
import com.matiasilveiro.automastichome.main.framework.mappers.toControlFeedback
import com.matiasilveiro.automastichome.main.framework.mappers.toRemoteActuator
import com.matiasilveiro.automastichome.main.framework.mappers.toRemoteSensor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNodesSource : NodesDataSource {

    companion object RetrofitConfig {
        val apiClient: ApiService = Retrofit.Builder()
            .baseUrl("https://192.168.56.1:5000")
            .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    override suspend fun getCentralNodesByUser(uid: String): MyResult<ArrayList<CentralNode>?> {
        try {
            val response = apiClient.getCentralNodesByUser(uid)
            // Check if response was successful.
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                val result = data.map { it.toCentralNode() }

                result.forEach {
                    Log.d("RetrofitNodesSource", "Node retrieved with name ${it.name}")
                }

                return MyResult.Success(ArrayList(result))
            } else {
                // Show API error.
                Log.d("Retrofit", "Error occurred: ${response.message()}")
                return MyResult.Success(arrayListOf())
            }
        } catch (e: Exception) {
            // Show API error. This is the error raised by the client.
            Log.d("Retrofit", "Error occurred: ${e.message}")
            return MyResult.Failure(e)
        }
    }

    override suspend fun getCentralNodesByAddress(address: String): MyResult<ArrayList<CentralNode>?> {
        try {
            val response = apiClient.getCentralNodesByAddress(address)
            // Check if response was successful.
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                val result = data.map { it.toCentralNode() }

                result.forEach {
                    Log.d("RetrofitNodesSource", "Node retrieved with name ${it.name}")
                }

                return MyResult.Success(ArrayList(result))
            } else {
                // Show API error.
                Log.d("Retrofit", "Error occurred: ${response.message()}")
                return MyResult.Success(arrayListOf())
            }
        } catch (e: Exception) {
            // Show API error. This is the error raised by the client.
            Log.d("Retrofit", "Error occurred: ${e.message}")
            return MyResult.Failure(e)
        }
    }

    override suspend fun createCentralNode(node: CentralNode): MyResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun setCentralNode(node: CentralNode): MyResult<Boolean> {
        try {
            val response = apiClient.setCentralNode("1",Gson().toJson(node))
            // Check if response was successful.
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                Log.d("RetrofitNodesSource", "Node created, ${data.status}, ${data.message}")

                return MyResult.Success(true)
            } else {
                // Show API error.
                Log.d("Retrofit", "Error occurred: ${response.message()}")
                return MyResult.Success(false)
            }
        } catch (e: Exception) {
            // Show API error. This is the error raised by the client.
            Log.d("Retrofit", "Error occurred: ${e.message}")
            return MyResult.Failure(e)
        }
    }

    override suspend fun deleteCentralNode(node: CentralNode): MyResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun createCentralNodeRole(uid: String, centralId: String, role: Int): MyResult<Boolean> {
        try {
            val response = apiClient.createCentralNodeRole(uid, centralId, role)
            // Check if response was successful.
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                Log.d("RetrofitNodesSource", "Node created, ${data.status}, ${data.message}")

                return MyResult.Success(true)
            } else {
                // Show API error.
                Log.d("Retrofit", "Error occurred: ${response.message()}")
                return MyResult.Success(false)
            }
        } catch (e: Exception) {
            // Show API error. This is the error raised by the client.
            Log.d("Retrofit", "Error occurred: ${e.message}")
            return MyResult.Failure(e)
        }
    }

    override suspend fun getRemoteActuatorsByCentral(uid: String): MyResult<ArrayList<RemoteActuator>?> {
        try {
            val response = apiClient.getRemoteActuatorsByCentral(uid)
            // Check if response was successful.
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                val result = data.map { it.toRemoteActuator() }

                result.forEach {
                    Log.d("RetrofitNodesSource", "Node retrieved with name ${it.name}")
                }

                return MyResult.Success(ArrayList(result))
            } else {
                // Show API error.
                Log.d("Retrofit", "Error occurred: ${response.message()}")
                return MyResult.Success(arrayListOf())
            }
        } catch (e: Exception) {
            // Show API error. This is the error raised by the client.
            Log.d("Retrofit", "Error occurred: ${e.message}")
            return MyResult.Failure(e)
        }
    }

    override suspend fun createRemoteActuator(node: RemoteActuator): MyResult<Boolean> {
        try {
            val response = apiClient.createRemoteActuator(Gson().toJson(node))
            // Check if response was successful.
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                Log.d("RetrofitNodesSource", "Node created, ${data.status}, ${data.message}")

                return MyResult.Success(true)
            } else {
                // Show API error.
                Log.d("Retrofit", "Error occurred: ${response.message()}")
                return MyResult.Success(false)
            }
        } catch (e: Exception) {
            // Show API error. This is the error raised by the client.
            Log.d("Retrofit", "Error occurred: ${e.message}")
            return MyResult.Failure(e)
        }
    }

    override suspend fun setRemoteActuator(node: RemoteActuator): MyResult<Boolean> {
        try {
            val response = apiClient.setRemoteActuator(node.centralUid, Gson().toJson(node))
            // Check if response was successful.
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                Log.d("RetrofitNodesSource", "Node created, ${data.status}, ${data.message}")

                return MyResult.Success(true)
            } else {
                // Show API error.
                Log.d("Retrofit", "Error occurred: ${response.message()}")
                return MyResult.Success(false)
            }
        } catch (e: Exception) {
            // Show API error. This is the error raised by the client.
            Log.d("Retrofit", "Error occurred: ${e.message}")
            return MyResult.Failure(e)
        }
    }

    override suspend fun setRemoteActuatorValue(node: RemoteActuator): MyResult<Boolean> {
        try {
            val response = apiClient.setRemoteActuatorValue(node.uid.toInt(), node.value)
            // Check if response was successful.
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                Log.d("RetrofitNodesSource", "Node created, ${data.status}, ${data.message}")

                return MyResult.Success(true)
            } else {
                // Show API error.
                Log.d("Retrofit", "Error occurred: ${response.message()}")
                return MyResult.Success(false)
            }
        } catch (e: Exception) {
            // Show API error. This is the error raised by the client.
            Log.d("Retrofit", "Error occurred: ${e.message}")
            return MyResult.Failure(e)
        }
    }

    override suspend fun deleteRemoteActuator(node: RemoteActuator): MyResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getRemoteSensorsByCentral(uid: String): MyResult<ArrayList<RemoteSensor>?> {
        try {
            val response = apiClient.getRemoteSensorsByCentral(uid)
            // Check if response was successful.
            if (response.isSuccessful && response.body() != null) {
                Log.d("RetrofitNodesSource", "Body ${response.body()}")
                val data = response.body()!!
                val result = data.map { it.toRemoteSensor() }

                result.forEach {
                    Log.d("RetrofitNodesSource", "Node retrieved with name ${it.name}")
                }

                return MyResult.Success(ArrayList(result))
            } else {
                // Show API error.
                Log.d("Retrofit", "Error occurred: ${response.message()}")
                return MyResult.Success(arrayListOf())
            }
        } catch (e: Exception) {
            // Show API error. This is the error raised by the client.
            Log.d("Retrofit", "Exception occurred: ${e}")
            return MyResult.Failure(e)
        }
    }

    override suspend fun createRemoteSensor(node: RemoteSensor): MyResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun setRemoteSensor(node: RemoteSensor): MyResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRemoteSensor(node: RemoteSensor): MyResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getRemoteControlsByCentral(uid: String): MyResult<ArrayList<ControlFeedback>?> {
        try {
            val response = apiClient.getRemoteControlsByCentral(uid)
            // Check if response was successful.
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                val result = data.map { it.toControlFeedback() }

                result.forEach {
                    Log.d("RetrofitNodesSource", "Control retrieved with name ${it.name}")
                }

                return MyResult.Success(ArrayList(result))
            } else {
                // Show API error.
                Log.d("Retrofit", "Error occurred: ${response.message()}")
                return MyResult.Success(arrayListOf())
            }
        } catch (e: Exception) {
            // Show API error. This is the error raised by the client.
            Log.d("Retrofit", "Error occurred: ${e.message}")
            return MyResult.Failure(e)
        }
    }

    override suspend fun createRemoteControl(
        sensor: RemoteSensor,
        actuator: RemoteActuator,
        control: ControlFeedback
    ): MyResult<Boolean> {
        try {
            val response = apiClient.createRemoteControl(actuator.uid.toInt(), sensor.uid.toInt(),Gson().toJson(control))
            // Check if response was successful.
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                Log.d("RetrofitNodesSource", "Control created, ${data.status}, ${data.message}")

                return MyResult.Success(true)
            } else {
                // Show API error.
                Log.d("Retrofit", "Error occurred: ${response.message()}")
                return MyResult.Success(false)
            }
        } catch (e: Exception) {
            // Show API error. This is the error raised by the client.
            Log.d("Retrofit", "Error occurred: ${e.message}")
            return MyResult.Failure(e)
        }
    }

    override suspend fun setRemoteControl(control: ControlFeedback): MyResult<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRemoteControl(control: ControlFeedback): MyResult<Boolean> {
        TODO("Not yet implemented")
    }


}