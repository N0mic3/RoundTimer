package com.example.roundtimer.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.roundtimer.R
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource

@Composable
fun GoogleSignInButton(
    isSigningIn: Boolean,
    onCredentialReceived: (String) -> Unit,
    onCredentialFailed: (String) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = remember(context) {
        CredentialManager.create(context)
    }
    val serverClientId = stringResource(R.string.default_web_client_id)

    Button(
        enabled = !isSigningIn,
        onClick = {
            coroutineScope.launch {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(
                        serverClientId
                    )
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                try {
                    val result = credentialManager.getCredential(
                        context = context,
                        request = request,
                    )

                    val credential = result.credential

                    if (
                        credential is CustomCredential &&
                        credential.type ==
                        GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                    ) {
                        val googleCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)

                        onCredentialReceived(googleCredential.idToken)
                    } else {
                        onCredentialFailed(
                            "Google did not return a valid sign-in credential.",
                        )
                    }
                } catch (_: GetCredentialException) {
                    onCredentialFailed(
                        "Google account selection was cancelled or failed.",
                    )
                }
            }
        },
    ) {
        Text(
            text = if (isSigningIn) {
                "Signing in..."
            } else {
                "Sign in with Google"
            },
        )
    }
}