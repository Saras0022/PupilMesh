package com.saras.pupilmesh.ui.screen.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saras.pupilmesh.R

@Composable
fun LoginScreen(viewModel: LoginScreenViewModel = hiltViewModel(), onSignInSuccess: () -> Unit) {

    val showPassword = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(6.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text("Zenithra", fontSize = 24.sp)
                Text("Welcome back", fontSize = 36.sp)
                Text("Please enter your details to sign in", fontSize = 12.sp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton({}, modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)) {
                        Icon(
                            ImageVector.vectorResource(R.drawable.google),
                            "Google",
                            tint = Color.Unspecified,
                            modifier = Modifier.requiredSize(36.dp)
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    OutlinedButton({}, modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)) {
                        Icon(
                            ImageVector.vectorResource(R.drawable.apple),
                            "Apple",
                            tint = Color.Unspecified,
                            modifier = Modifier.requiredSize(36.dp)
                        )
                    }
                }

                Box(modifier = Modifier.fillMaxWidth()) {
                    HorizontalDivider(modifier = Modifier.align(Alignment.Center))
                    Text("OR", modifier = Modifier.align(Alignment.Center))
                }
                OutlinedTextField(
                    value = viewModel.email.value,
                    onValueChange = viewModel::onEmailChange,
                    placeholder = { Text("Your Email Address") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = viewModel.password.value,
                    onValueChange = viewModel::onPasswordChange,
                    placeholder = { Text("Password") },
                    trailingIcon = {
                        IconButton({ showPassword.value = !showPassword.value }) {
                            when (showPassword.value) {
                                false -> Icon(Icons.Outlined.Visibility, "")
                                true -> Icon(Icons.Outlined.VisibilityOff, "")
                            }
                        }
                    },
                    visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "Forgot password?",
                    textDecoration = TextDecoration.Underline,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable {}
                )
                FilledTonalButton({
                    viewModel.onSignIn(
                        onSuccess = onSignInSuccess,
                        onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                    )
                }) {
                    Text(
                        "Sign in",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row {
                    Text("Don't have an account? ", fontSize = 12.sp)
                    Text(
                        "Sign Up",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {})
                }
            }
        }
    }
}