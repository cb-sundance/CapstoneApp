package com.example.capstoneapp

// Minimal local repository that provides fun facts / demo data.
// Keeps things simple and avoids networking errors at submission time.

class PhotoRepository {
    fun getFunFact(): String {
        return "I love learning how games are made and building small prototypes."
    }

    // If you later want a suspend function that fetches from an API, we can add it,
    // but for now this local method prevents unresolved reference errors.
}
