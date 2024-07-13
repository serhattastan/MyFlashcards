package com.cloffygames.myflashcards.ui.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cloffygames.myflashcards.R
import dagger.hilt.android.AndroidEntryPoint

// MainActivity, Hilt tarafından sağlanan bağımlılıkları kullanmak için AndroidEntryPoint ile işaretlenmiştir
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // activity_main layout dosyasını şişiriyor
        setContentView(R.layout.activity_main)
    }
}