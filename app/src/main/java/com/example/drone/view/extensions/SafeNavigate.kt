import android.util.Log
import androidx.annotation.IdRes
import androidx.navigation.NavController

// Basic section
fun NavController.safeNavigate(@IdRes navigationId: Int) {
    try {
        this.navigate(navigationId)
    } catch (e: IllegalArgumentException) {
        e.localizedMessage?.let { Log.e("error", it) }
    }
}