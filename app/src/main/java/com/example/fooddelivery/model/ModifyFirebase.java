package com.example.fooddelivery.model;

import android.net.Uri;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


public class ModifyFirebase {
    private static final String TAG = "firebaseFirstore";
    private Object object;
    private String docRef;
    private Uri image[];
    private String collectionPath = "";
    private FirebaseFirestore root = FirebaseFirestore.getInstance();
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private boolean checkUsername = false;
    private boolean uIDCheck = false;



    public ModifyFirebase() {
    };

    public void insertDataFirestore(String id) {  // Thêm vào Firestore
        // CollectionPath là đường dẫn đến nơi cần thêm
        // ID là khóa, không cần thêm cũng được, nó sự tự động tạo ra
        // Object là đối tượng cần thêm, thường là class
        root.collection(collectionPath).document(id).set(object)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        docRef = id;
                    }
                });
    }

    private void uploadImage() { // Upload ảnh lên Storage rồi lưu đường dẫn ở Firestore
        for (Uri img : image) {
            if (img != null) {
                StorageReference fileRef = reference.child("Images" + System.currentTimeMillis() + "." + getExtension(img));
                fileRef.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Map<String, String> image = new HashMap<>();
                                image.put("imageLink", uri.toString());
                                image.put("timeUpload", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
                                root.collection(collectionPath + "/" + docRef + "/Photos").add(image);
                            }
                        });
                    }
                });
            }
        }
    }

    private String getExtension(Uri uri) {
        String stringUri = uri.toString();
        return stringUri.substring(stringUri.lastIndexOf("."));
    }

    public boolean checkUID(String uID) {
        uIDCheck = false;
        root.collection("User").document(uID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        uIDCheck = true;
                        documentSnapshot.get("name");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        return uIDCheck;
    }

    public boolean checkEmail(String email) {
        root.collection("User")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    }
                });
        return false;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getDocRef() {
        return docRef;
    }

    public void setDocRef(String docRef) {
        this.docRef = docRef;
    }

    public Uri[] getImage() {
        return image;
    }

    public void setImage(Uri[] image) {
        this.image = image;
    }

    public String getCollectionPath() {
        return collectionPath;
    }

    public void setCollectionPath(String collectionPath) {
        this.collectionPath = collectionPath;
    }

    public FirebaseFirestore getRoot() {
        return root;
    }

    public void setRoot(FirebaseFirestore root) {
        this.root = root;
    }

    public StorageReference getReference() {
        return reference;
    }

    public void setReference(StorageReference reference) {
        this.reference = reference;
    }

    public boolean isCheckUsername() {
        return checkUsername;
    }
}
