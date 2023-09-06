package com.gateway.networkparams

import android.os.Build
import android.os.Bundle
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.gateway.networkparam.NetworkParams
import com.gateway.networkparam.entity.CellLte
import com.gateway.networkparam.entity.util.NetworkParamsOperator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


@RequiresApi(Build.VERSION_CODES.Q)
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.Main).launch {
            test {
                findViewById<EditText>(R.id.text).setText(it.toString())
            }
        }
    }

    private suspend fun test(onUpdate: (List<CellLte>) -> Unit) {
        NetworkParams(this).run {
            withContext(Dispatchers.IO) {
                requestCellLteUpdates(
                    networkOperator = NetworkParamsOperator.Fastlink,
                    onUpdate = {
                        Timber.i(it.toString())
                        onUpdate(lastCellsLte)
                    }
                )
            }
        }
    }
}
