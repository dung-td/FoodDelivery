package com.example.fooddelivery.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.fooddelivery.R;
import com.example.fooddelivery.activity.PersonalInfoActivity;
import com.example.fooddelivery.activity.VerifyPhoneActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.v1.StructuredQuery;
import com.google.type.DateTime;
import com.squareup.picasso.Picasso;

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
    public ArrayList<SearchString> searchList = new ArrayList<>();
    public ArrayList<Product> cartList = new ArrayList<>();
    public ArrayList<Product> productList = new ArrayList<Product>();
    public ArrayList<String> watchedList = new ArrayList<>();
    public ArrayList<String> favouriteProductList = new ArrayList<String>();
    public ArrayList<Merchant> merchantList = new ArrayList<Merchant>();
    public ArrayList<Voucher> voucherList = new ArrayList<Voucher>();
    public ArrayList<Voucher> availableVoucherList = new ArrayList<Voucher>();
    public ArrayList<Orders> ordersList = new ArrayList<>();
    private FirebaseFirestore root = FirebaseFirestore.getInstance();
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private final boolean checkUsername = false;
    private boolean uIDCheck = false;
    private User user = new User();

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

    public void addProductToCart(String productId) {
    }

    public void addProductToWatched(String productId) {
        Map<String, String> product = new HashMap<>();
        product.put("ProductId", productId);
        root.collection("User/" + userId + "/Watched/")
                .document(productId)
                .set(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
    }

    public void removeWatchedProductData(final OnGetDataListener listener) {
        listener.onStart();
        for (String productId : watchedList) {
            root.collection("User/" + userId + "/Watched/")
                    .document(productId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            watchedList.remove(productId);
                        }
                    });
        }
        listener.onSuccess();
    }

    public void getWatchedProductList(final OnGetDataListener listener) {
        listener.onStart();
        root.collection("User/" + userId + "/Watched")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document == null)
                                break;
                            watchedList.add((String) document.get("ProductId"));
                        }
                        listener.onSuccess();
                    }
                });
    }

    public void getVoucherList() {
        root.collection("User/" + userId + "/Voucher/")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Log.d("GOT" , document.getId());
                            Voucher voucher = new Voucher();
                            voucher.setStatus(document.get("status").toString());
                            voucher.setId(document.getId());
                            root.collection("Voucher/")
                                    .document(document.getId())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            voucher.setCode(documentSnapshot.get("code").toString());
                                            voucher.setTitle(documentSnapshot.get("title").toString());
                                            voucher.setDate(documentSnapshot.get("date").toString());
                                            voucher.setValues((List<String>) documentSnapshot.get("value"));
                                            voucher.setDetails((List<String>) documentSnapshot.get("details"));
                                            voucherList.add(voucher);
                                        }
                                    });
                        }
                    }
                });
    }

    public void getAvailableVoucherList() {
        root.collection("Voucher/")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Voucher voucher = new Voucher();
                            voucher.setId(document.getId());
                            voucher.setCode(document.get("code").toString());
                            voucher.setTitle(document.get("title").toString());
                            voucher.setDate(document.get("date").toString());
                            voucher.setValues((List<String>) document.get("value"));
                            voucher.setDetails((List<String>) document.get("details"));
                            availableVoucherList.add(voucher);
                        }
                    }
                });
    }

    public void getComment() {
        int index = 0;
        for (Product product : productList) {
            ArrayList<Comment> comments = new ArrayList<Comment>();
            int finalIndex = index;
            root.collection("Product/" + product.getId() + "/Comment/")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            Log.d(TAG, "Got comment from product");
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                if (document == null)
                                    break;
                                Comment comment = new Comment();
                                comment.setiD(document.getId());
                                comment.setUserName(document.get("userName").toString());
                                comment.setDate(document.get("date").toString());
                                comment.setDetails(document.get("details").toString());
                                comment.setRating(document.get("rating").toString());
                                comments.add(comment);
                            }
                            productList.get(finalIndex).setCommentList(comments);
                        }
                    });
            index++;
        }
    }

    public void getData(final OnGetDataListener listener) {
        listener.onStart();
        //Load Orders
        getListOrdersOfUser();

        //Load merchant list
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
                                                if (((String) document.get("Image_Link")) != null) {
                                                    merchantImages.add(Uri.parse((String) document.get("Image_Link")));
                                                    break;
                                                }
                                            }
                                            merchant.setImage(merchantImages);
                                            merchantList.add(merchant);
                                        }
                                    });
                        }

                        //Load Product list
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
//                                            product.setMerchant((Merchant) findMerchantFromId(((String) document.get("Merchant")).substring(9)));
                                            product.setRating((String) document.get("Rating"));
                                            product.setType((String) document.get("Type"));

                                            //Load product images
                                            ArrayList<Uri> images = new ArrayList<Uri>();
                                            root.collection("Product/" + product.getId() + "/Photos/")
                                                    .get()
                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                                if (document == null)
                                                                    break;
                                                                if (((String) document.get("Image_Link")) != null) {
                                                                    images.add(Uri.parse((String) document.get("Image_Link")));
                                                                    break;
                                                                }
                                                            }
                                                            product.setImage(images);
                                                            product.setMerchant((Merchant) findMerchantFromId(((String) document.get("Merchant")).substring(9)));
                                                            productList.add(product);
                                                            if (productList.size() > 1 && merchantList.size() > 1)
                                                                listener.onSuccess();
                                                        }
                                                    });
                                        }
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

        //Load product in cart


    }

    public void loadFullListMerchantImage(Merchant merchant, final OnGetDataListener listener) {
        listener.onStart();
        ArrayList<Uri> merchantImages = new ArrayList<Uri>();
        root.collection("Merchant/" + merchant.getId() + "/Photos/")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document == null)
                                break;
                            if (((String) document.get("Image_Link")) != null) {
                                merchantImages.add(Uri.parse((String) document.get("Image_Link")));
                            }
                        }
                        merchant.setImage(merchantImages);
                        listener.onSuccess();
                    }
                });
    }

    public void getSearchData(final OnGetDataListener listener) {
        listener.onStart();
        root.collection("User/" + userId + "/Search/")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document == null)
                                break;
                            SearchString data = new SearchString();
                            data.setId((String) document.getId());
                            data.setDetail((String) document.get("SearchString"));
                            searchList.add(data);
                        }
                        listener.onSuccess();
                    }
                });
    }

    public void removeSearchData(final OnGetDataListener listener) {
        listener.onStart();
        for (SearchString search : searchList) {
            root.collection("User/" + userId + "/Search/")
                    .document(search.getId())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            searchList.remove(search);
                        }
                    });
        }
        listener.onSuccess();
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

    public void getUserInfo() {
        root.collection("User").document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                user.Email = document.get("email").toString();
                                user.Address = document.get("address").toString();
                                user.First_Name = document.get("first_Name").toString();
                                user.Last_Name = document.get("last_Name").toString();
                                user.Phone_Number = document.get("phone_Number").toString();

                                StorageReference fileRef = reference.child("UserImage/"+ userId);
                                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        user.setProfileImage(uri);
                                    }
                                });
                            }
                        }
                    }
                });
    }

    public void addVoucherToList(Context context, String voucherId) {
        Map<String, String> voucher = new HashMap<>();
        voucher.put("status", "Hiện có");
        root.collection("User/" + userId + "/Voucher/")
                .document(voucherId)
                .set(voucher)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Thêm voucher thành công, vào ví để kiểm tra!", Toast.LENGTH_SHORT);
                    }
                });
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

    //region ORDERS
    ArrayList <Map<String, String>> listMap = new ArrayList<>();
    public ArrayList <OrderItem> getListOrderedItems(String orderID) {
        ArrayList <OrderItem> orderItemArrayList = new ArrayList<>();


        root.collection("User/" + userId + "/Order/").document(orderID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot != null)
                        {
                            listMap = (ArrayList <Map<String, String>>)documentSnapshot.get("listItems");

                            for (Map item : listMap) {
                                Product product = new Product();
                                product = getProductById(item.get("product").toString());

                                OrderItem orderItem = new OrderItem(
                                        orderID,
                                        product,
                                        Integer.parseInt(item.get("quantity").toString()),
                                        Integer.parseInt(item.get("price").toString()),
                                        findCommentById(item.get("product").toString(), item.get("comment").toString()),
                                        item.get("size").toString()
                                );

                                orderItemArrayList.add(orderItem);
                            }
                        }
                    }
                });
        return orderItemArrayList;
    }

    public Product getProductById(String productID) {
        Product product = new Product();
        root.collection("Product/").document(productID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                product.setId(productID);
                                product.setEn_Name(document.get("Name_En").toString());
                                product.setName(document.get("Name").toString());
                                product.setMerchant((Merchant) findMerchantFromId(((String) document.get("Merchant")).substring(9)));

                                ArrayList<Uri> images = new ArrayList<Uri>();
                                root.collection("Product/" + product.getId() + "/Photos/")
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                    if (document == null)
                                                        break;
                                                    if (((String) document.get("Image_Link")) != null) {
                                                        images.add(Uri.parse((String) document.get("Image_Link")));
                                                        break;
                                                    }
                                                }
                                                product.setImage(images);
                                            }
                                        });
                            }
                        }
                    }
                });

        return product;
    }

    public Comment findCommentById(String productId, String commentId){
        Comment comment = new Comment();

        if (commentId.equals("null"))
            return null;

        root.collection("Product/" + productId + "/Comment")
                .document(commentId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot != null)
                        {
                            comment.setiD(commentId);
                            comment.setDate(documentSnapshot.get("date").toString());
                            comment.setDetails(documentSnapshot.get("details").toString());
                            comment.setRating(documentSnapshot.get("rating").toString());
                            comment.setUserName(documentSnapshot.get("userName").toString());
                        }
                    }
                });

        return comment;
    }

    public Orders getOrderById(String orderID){
        Orders orders = new Orders();
        root.collection("User/" + userId + "/Order")
                .document(orderID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot != null)
                            {
                                orders.orderID = orderID;
                                orders.date = documentSnapshot.getTimestamp("date");
                                orders.discount = Integer.parseInt(documentSnapshot.get("discount").toString());
                                orders.status = documentSnapshot.get("status").toString();
                                orders.freightCost=Integer.parseInt(documentSnapshot.get("freight_cost").toString());
                                orders.listOrderItems = getListOrderedItems(orderID);
                                orders.method = documentSnapshot.get("payment_method").toString();
                                orders.status = documentSnapshot.get("status").toString();
                                orders.totalAmount = Integer.parseInt(documentSnapshot.get("total_amount").toString());
                                orders.voucherID = documentSnapshot.get("voucher").toString();
                            }

                    }
                });
        return orders;
    }

    public void getListOrdersOfUser() {
        root.collection("User/" + userId + "/Order")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document == null)
                                break;
                           Orders newOrders = new Orders();
                           newOrders = getOrderById(document.getId());
                           ordersList.add(newOrders);
                        }

                    }
                });
    }
    //endregion
    

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Orders> getOrdersList() {
        return ordersList;
    }

    public void setOrdersList(ArrayList<Orders> ordersList) {
        this.ordersList = ordersList;
    }
}
