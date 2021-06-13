package com.example.fooddelivery.model;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.fooddelivery.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ModifyFirebase {
    private static final String TAG = "firebaseFirstore";
    private Object object;
    private String docRef;
    private String userId;
    private Uri[] image;
    private String collectionPath = "";
    public ArrayList<Product> productList = new ArrayList<Product>();
    public ArrayList<String> favouriteProductList = new ArrayList<String>();
    public ArrayList<Merchant> merchantList = new ArrayList<Merchant>();
    public ArrayList<Voucher> voucherList = new ArrayList<Voucher>();
    private FirebaseFirestore root = FirebaseFirestore.getInstance();
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private final boolean checkUsername = false;
    private boolean uIDCheck = false;

    public ModifyFirebase() {
    }

    public void addProductToFavourite(Context context, String productId) {
        Map<String, String> product = new HashMap<>();
        product.put("ProductId", productId);
        root.collection("User/" + userId + "/Favourite/")
                .document(productId)
                .set(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, context.getString(R.string.add_favourite_success), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void removeProductFromFavourite(Context context, String productId) {
        root.collection("User/" + userId + "/Favourite/")
                .document(productId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, context.getString(R.string.remove_favourite_success), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    public void getVoucher() {
        root.collection("User/" + userId + "/Voucher/")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "get from user");
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document == null)
                                break;
                            Voucher voucher = new Voucher();
                            voucher.setStatus(document.get("status").toString());
                            root.collection("Voucher")
                                    .document(document.getId())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Log.d(TAG, "get from voucher");
                                            voucher.setCode(documentSnapshot.get("code").toString());
                                            voucher.setTitle(documentSnapshot.get("title").toString());
                                            voucher.setDate(documentSnapshot.get("date").toString());
                                            voucher.setValues((List<String>)documentSnapshot.get("value"));
                                            voucher.setDetails((List<String>)documentSnapshot.get("details"));
                                            voucherList.add(voucher);
                                        }
                                    });
                        }
                    }
                });
    }

    public void getData(final OnGetDataListener listener) {
        listener.onStart();
        root.collection("Merchant/")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document == null)
                                break;
                            Merchant merchant = new Merchant();
                            merchant.setName((String) document.get("Name"));
                            merchant.setAddress((String) document.get("Address"));
                            merchant.setId((String) document.getId());

                            ArrayList<Uri> merchantImages = new ArrayList<Uri>();
                            root.collection("Merchant/" + merchant.getId() + "/Photos/")
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                if (document == null)
                                                    break;
                                                if (((String) document.get("Image_Link")) != null)
                                                    merchantImages.add(Uri.parse((String) document.get("Image_Link")));
                                            }
                                            merchant.setImage(merchantImages);
                                            merchantList.add(merchant);
                                        }
                                    });
                        }

                        root.collection("Product/")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            if (document == null)
                                                break;
                                            Product product = new Product();
                                            product.setId((String) document.getId());
                                            product.setName((String) document.get("Name"));
                                            product.setEn_Name((String) document.get("Name_En"));
                                            product.setStatus((String) document.get("Status"));
                                            product.setPrice((ArrayList<String>) document.get("Price"));
                                            product.setProductSize((ArrayList<String>) document.get("Size"));
                                            product.setMerchant((Merchant) findMerchantFromId(((String) document.get("Merchant")).substring(9)));
                                            product.setRating((String) document.get("Rating"));

                                            ArrayList<Uri> images = new ArrayList<Uri>();
                                            root.collection("Product/" + product.getId() + "/Photos/")
                                                    .get()
                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                                if (document == null)
                                                                    break;
                                                                if (((String) document.get("Image_Link")) != null)
                                                                    images.add(Uri.parse((String) document.get("Image_Link")));
                                                            }
                                                            product.setImage(images);
                                                            productList.add(product);
                                                            if (productList.size() > 1 && merchantList.size() > 0)
                                                                listener.onSuccess();
                                                        }
                                                    });
                                        }
//                                        listener.onSuccess();
                                    }
                                });
                    }
                });


        root.collection("User/" + userId + "/Favourite")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document == null)
                                break;
                            favouriteProductList.add((String) document.get("ProductId"));
                        }
                    }
                });
    }

    public void insertDataFirestore(String id) {
        root.collection(collectionPath).document(id).set(object)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        docRef = id;
                    }
                });
    }


    private Merchant findMerchantFromId(String id) {
        for (Merchant mer : merchantList) {
            if (mer.getId().equals(id))
                return mer;
        }
        return null;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
