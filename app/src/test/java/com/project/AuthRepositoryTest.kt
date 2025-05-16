import com.google.firebase.auth.FirebaseAuth
import com.project.data.repository.AuthRepository
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test

class AuthRepositoryTest (){
    private val auth = FirebaseAuth.getInstance()
    private val authRepository = AuthRepository(auth)
    private val email = "test@example.com"
    private val password = "password"
    private val invalidEmail = "invalid@example.com"
    private val invalidPassword = "invalid"
    private val wrongPassword = "wrong"

    @Test
    fun testLoginSuccess() = runBlocking {
        val result = authRepository.login(email, password)
        assertTrue(result.isSuccess)
    }
}