package xyz.eddief.halfway.data.service

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import xyz.eddief.halfway.data.models.UserWithLocations
import xyz.eddief.halfway.utils.dLog
import javax.inject.Inject

class FirestoreService @Inject constructor(private val db: FirebaseFirestore) {

    private val usersCollection get() = db.collection("users")

    fun updateUser(userWithLocations: UserWithLocations) {
        usersCollection.document(userWithLocations.user.userId).set(userWithLocations)
    }

    fun fetchUser(userId: String): UserWithLocations? {
        val documentReference: DocumentReference = usersCollection.document(userId)
        val userData = Tasks.await(documentReference.get())
        dLog("uuuuuuuuu userData ${userData.data}")
        return userData.toObject<UserWithLocations>()
    }
}