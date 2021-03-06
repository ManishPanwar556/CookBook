package com.example.cookbook

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import com.example.cookbook.databinding.FragmentSignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

const val RC_SIGNIN = 123

class SignInFragment : Fragment() {
    lateinit var binding: FragmentSignInBinding
    lateinit var signInClient: GoogleSignInClient
    val db by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater)
        binding.signInBtn.setOnClickListener {
            signInUser()
        }
        binding.skipSignIn.setOnClickListener {
            updateUI()
        }
        return binding.root
    }

    private fun signInUser() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        signInClient = GoogleSignIn.getClient(requireContext(), gso)
        val intent = signInClient.signInIntent
        startActivityForResult(intent, RC_SIGNIN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGNIN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            binding.progressBar.visibility=View.VISIBLE
            handleSignInResult(task)
        }
    }


    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            uploadUserToFirebase(account?.idToken)
        } catch (e: ApiException) {

        }
    }

    private fun uploadUserToFirebase(token: String?) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnSuccessListener {
            val user = it.user
            addUser(user)
        }
    }

    private fun addUser(user: FirebaseUser?) {
        db.collection("users").document(user!!.uid).get().addOnCompleteListener {
            val document=it.getResult()
            if(document!=null&&document.exists()){
                updateUI()
            }
            else{
                user.let {
                    val user = User(it.displayName, it.photoUrl?.toString(), listOf(), listOf())
                    db.collection("users").document(it.uid).set(user).addOnSuccessListener {
                        updateUI()
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), "SignIn Failure", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun updateUI() {
        binding.progressBar.visibility=View.INVISIBLE
        val supportFragmentManager=requireActivity().supportFragmentManager
        supportFragmentManager.commit {
            replace(R.id.nav_host_fragment,CuisineFragment())
            setReorderingAllowed(true)
        }
    }

}
