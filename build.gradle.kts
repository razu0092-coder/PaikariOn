<<<<<<< HEAD
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.google.devtools.ksp) apply false
  alias(libs.plugins.roborazzi) apply false
  alias(libs.plugins.secrets) apply false
  alias(libs.plugins.google.services) apply false
=======
// Top-level build file
plugins {
  id("com.android.application") version "8.2.2" apply false
  id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
  id("com.google.devtools.ksp") version "2.0.0-1.0.21" apply false
  id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
  id("com.google.gms.google-services") version "4.4.1" apply false
>>>>>>> a88dcbf4fbda35ea84204e8f626d106bbb113a0e
}
