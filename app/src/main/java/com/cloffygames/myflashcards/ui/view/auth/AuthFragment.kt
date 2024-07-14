package com.cloffygames.myflashcards.ui.view.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.cloffygames.myflashcards.R
import com.cloffygames.myflashcards.databinding.FragmentAuthBinding
import com.cloffygames.myflashcards.ui.viewmodel.AuthState
import com.cloffygames.myflashcards.ui.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint // Bu sınıf, kullanıcı kimlik doğrulama işlemlerini yönetmek için kullanılır ve Hilt tarafından sağlanan bağımlılıkları kullanır.
class AuthFragment : Fragment() {
    // AuthViewModel örneği, Hilt tarafından sağlanır
    private val authViewModel: AuthViewModel by viewModels()
    // Google Sign-In client tanımlanıyor
    private lateinit var googleSignInClient: GoogleSignInClient
    // View Binding için FragmentAuthBinding nesnesi
    private lateinit var binding: FragmentAuthBinding

    companion object {
        private const val TAG = "AuthFragment" // Log için tag
        // Email doğrulama için regex pattern
        private val EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE
        )
    }

    // Google Sign-In işlemi için launcher
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
            authViewModel.firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.w(TAG, "Google sign in failed", e)
            Toast.makeText(context, getString(R.string.google_sign_in_failed, e.message), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // FragmentAuthBinding ile layout şişiriliyor
        binding = FragmentAuthBinding.inflate(inflater, container, false)

        setupUI() // Kullanıcı arayüzünü ayarlayan metod çağrılıyor

        // Google Sign-In seçenekleri ayarlanıyor
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // Google Sign-In client oluşturuluyor
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // authState LiveData'sını gözlemliyoruz
        authViewModel.authState.observe(viewLifecycleOwner, Observer { authState ->
            when (authState) {
                is AuthState.Authenticated -> {
                    // Oturum açıldığında home fragment'a yönlendiriyoruz
                    findNavController().navigate(R.id.action_authFragment_to_homeFragment)
                }
                is AuthState.Unauthenticated -> {
                    // Giriş ekranında kalıyoruz
                }
                is AuthState.Error -> {
                    // Hata durumunda mesaj gösteriyoruz
                    Toast.makeText(context, authState.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        return binding.root
    }

    private fun setupUI() {
        // Giriş butonuna tıklanma işlemi
        binding.signInButton.setOnClickListener {
            val email = binding.signInEmailEditText.text.toString().trim()
            val password = binding.signInPasswordEditText.text.toString().trim()
            if (validateEmailAndPassword(email, password, isSignIn = true)) {
                authViewModel.signIn(email, password)
            }
        }

        // Kayıt ol butonuna tıklanma işlemi
        binding.signUpButton.setOnClickListener {
            val email = binding.signUpEmailEditText.text.toString().trim()
            val password = binding.signUpPasswordEditText.text.toString().trim()
            val confirmPassword = binding.editTextTextPassword2.text.toString().trim()
            if (validateEmailAndPassword(email, password, isSignIn = false) && validateConfirmPassword(password, confirmPassword)) {
                authViewModel.signUp(email, password)
            }
        }

        // Kayıt ol ekranına geçiş işlemi
        binding.goToSignUpTextView.setOnClickListener {
            binding.signInLayout.visibility = View.GONE
            binding.signUpLayout.visibility = View.VISIBLE
        }

        // Giriş yap ekranına geçiş işlemi
        binding.goToSignInTextView.setOnClickListener {
            binding.signUpLayout.visibility = View.GONE
            binding.signInLayout.visibility = View.VISIBLE
        }

        // Google ile giriş yap butonuna tıklanma işlemi
        binding.loginButtonGoogleSignIn.setOnClickListener {
            signInWithGoogle()
        }

        // Misafir olarak giriş yap butonuna tıklanma işlemi
        binding.guestButton.setOnClickListener {
            authViewModel.signInAsGuest()
        }
    }

    // Email ve şifre doğrulama işlemi
    private fun validateEmailAndPassword(email: String, password: String, isSignIn: Boolean): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            if (isSignIn) {
                binding.signInEmailEditText.error = getString(R.string.email_required)
            } else {
                binding.signUpEmailEditText.error = getString(R.string.email_required)
            }
            isValid = false
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            if (isSignIn) {
                binding.signInEmailEditText.error = getString(R.string.valid_email_required)
            } else {
                binding.signUpEmailEditText.error = getString(R.string.valid_email_required)
            }
            isValid = false
        }

        if (password.isEmpty()) {
            if (isSignIn) {
                binding.signInPasswordEditText.error = getString(R.string.password_required)
            } else {
                binding.signUpPasswordEditText.error = getString(R.string.password_required)
            }
            isValid = false
        } else if (password.length < 6) {
            if (isSignIn) {
                binding.signInPasswordEditText.error = getString(R.string.password_length_error)
            } else {
                binding.signUpPasswordEditText.error = getString(R.string.password_length_error)
            }
            isValid = false
        }

        return isValid
    }

    // Şifrelerin eşleştiğini doğrulama işlemi
    private fun validateConfirmPassword(password: String, confirmPassword: String): Boolean {
        if (password != confirmPassword) {
            binding.editTextTextPassword2.error = getString(R.string.password_mismatch)
            return false
        }
        return true
    }

    // Google ile giriş yapma işlemini başlatan metod
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }
}
