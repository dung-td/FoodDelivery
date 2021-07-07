package com.example.fooddelivery.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fooddelivery.R;
import com.example.fooddelivery.fragment.NotificationFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class ModifyFirebase {
    private static final String TAG = "firebaseFirstore";
    private static final String ORDER_TAG = "ORDER";
    private Object object;
    private String docRef;
    private String userId;
    private Uri[] image;
    private String collectionPath = "";
    public ArrayList<MyNotification> notifications = new ArrayList<>();
    public ArrayList<SearchString> searchList = new ArrayList<>();
    public ArrayList<ChosenItem> cartList = new ArrayList<>();
    public ArrayList<Product> productList = new ArrayList<Product>();
    public ArrayList<String> watchedList = new ArrayList<>();
    public ArrayList<String> favouriteProductList = new ArrayList<String>();
    public ArrayList<Merchant> merchantList = new ArrayList<Merchant>();
    public ArrayList<Voucher> voucherList = new ArrayList<Voucher>();
    public ArrayList<Voucher> availableVoucherList = new ArrayList<Voucher>();
    public ArrayList<ArrayList<Orders>> orderList = new ArrayList<>();
    private FirebaseFirestore root = FirebaseFirestore.getInstance();
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private final boolean checkUsername = false;
    private boolean uIDCheck = false;
    private User user = new User();
    private int count;

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

    public void removeProductFromCartWithContext(ChosenItem product, Context context) {
        final String[] documentID = {""};
        root.collection("User/" + userId + "/Cart/")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document.get("ProductId").toString().equals(product.getProduct().getId())
                                    && document.get("Size").toString().equals(product.getSize())) {
                                documentID[0] = document.getId();
                                break;
                            }
                        }
                        root.collection("User/" + userId + "/Cart/")
                                .document(documentID[0])
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, context.getString(R.string.remove_cart_success), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }

    public void removeProductFromCart() {
        for (ChosenItem product : cartList) {
            final String[] documentID = {""};
            root.collection("User/" + userId + "/Cart/")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                if (document.get("ProductId").toString().equals(product.getProduct().getId())
                                        && document.get("Size").toString().equals(product.getSize())) {
                                    documentID[0] = document.getId();
                                    break;
                                }
                            }
                            root.collection("User/" + userId + "/Cart/")
                                    .document(documentID[0])
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    });
                        }
                    });
        }
        cartList.clear();
    }

    public void addProductToCart(ChosenItem product, Context context) {
        Map<String, String> item = new HashMap<>();
        item.put("ProductId", product.getProduct().getId());
        item.put("Quantity", product.getQuantity());
        item.put("Size", product.getSize());
        root.collection("User/" + userId + "/Cart/")
                .document()
                .set(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, context.getString(R.string.add_cart_success), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateProductQuantityInCart(ChosenItem product) {
        final String[] documentID = {""};
        Map<String, Object> item = new HashMap<>();
        item.put("Quantity", product.getQuantity());
        root.collection("User/" + userId + "/Cart/")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document.get("ProductId").toString().equals(product.getProduct().getId())
                                    && document.get("Size").toString().equals(product.getSize())) {
                                documentID[0] = document.getId();
                                break;
                            }
                        }
                        root.collection("User/" + userId + "/Cart/")
                                .document(documentID[0])
                                .update(item)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                });
                    }
                });
    }

    public void getProductInCart() {
        cartList = new ArrayList<>();
        root.collection("User/" + userId + "/Cart/")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document == null)
                                break;
                            ChosenItem item = new ChosenItem();
                            item.getProduct().setId((String) document.get("ProductId"));
                            item.setSize((String) document.get("Size"));
                            item.setQuantity((String) document.get("Quantity"));
                            cartList.add(item);
                        }
                    }
                });
    }

    public void addUserNewOrder(String discount, String freight_cost, String time, String totalAmount, String voucherId,
                                final OnGetDataListener listener) {
        listener.onStart();
        String documentId = root.collection("User/" + userId + "/Order/").document().getId();
        Map<String, Object> item = new HashMap<>();
        item.put("user_id", userId);
        item.put("user_name", String.format("%s %s", user.getLast_Name(), user.getFirst_Name()));
        item.put("user_phone_number", user.getPhone_Number());
        item.put("user_address", user.getAddress().getAddressLine(0).toString());
        item.put("discount", discount);
        item.put("freight_cost", freight_cost);
        item.put("payment_method", "COD");
        item.put("status", "Pending");
        item.put("time", time);
        item.put("total_amount", totalAmount);
        item.put("voucher", voucherId);
        setVoucherUsed(voucherId);
        item.put("discount", discount);

        //add to user collection
        root.collection("User/" + userId + "/Order/")
                .document(documentId)
                .set(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        HashMap<String, Object> itemDetail = new HashMap<>();
                        for (ChosenItem product : cartList) {
                            itemDetail = new HashMap<>();
                            String price = "";
                            if (product.getProduct().getProductSize().get(0) != null) {
                                price = product.getProduct().getPrice().get(product.getProduct().getProductSize().indexOf(product.getSize()));
                            } else {
                                price = product.getProduct().getPrice().get(0);
                            }

                            itemDetail.put("comment", "null");
                            itemDetail.put("price", Integer.parseInt(price));
                            itemDetail.put("product", product.getProduct().getId());
                            itemDetail.put("quantity", Integer.parseInt(product.getQuantity()));
                            itemDetail.put("size", product.getSize());
                            root.collection("User/" + userId + "/Order/")
                                    .document(documentId).update("listItems", FieldValue.arrayUnion(itemDetail));
                        }
//                        listener.onSuccess();
                    }
                });

        //add to merchant collection

        root.collection("Merchant/" + cartList.get(0).getProduct().getMerchant().getId() + "/Order/")
                .document(documentId)
                .set(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        HashMap<String, Object> itemDetail = new HashMap<>();
                        for (ChosenItem product : cartList) {
                            itemDetail = new HashMap<>();
                            String price = "";
                            if (product.getProduct().getProductSize().get(0) != null) {
                                price = product.getProduct().getPrice().get(product.getProduct().getProductSize().indexOf(product.getSize()));
                            } else {
                                price = product.getProduct().getPrice().get(0);
                            }

                            itemDetail.put("comment", "null");
                            itemDetail.put("price", Integer.parseInt(price));
                            itemDetail.put("product", product.getProduct().getId());
                            itemDetail.put("quantity", Integer.parseInt(product.getQuantity()));
                            itemDetail.put("size", product.getSize());
                            root.collection("Merchant/" + cartList.get(0).getProduct().getMerchant().getId() + "/Order/")
                                    .document(documentId).update("listItems", FieldValue.arrayUnion(itemDetail));
                        }
                        removeProductFromCart();
                        listener.onSuccess();
                    }
                });

        //add notification
        MyNotification notification = new MyNotification();
        //Set vietnamese content
        notification.setTitle_vn("Cập nhật đơn hàng");
        notification.setDesc_vn("Đơn hàng " + documentId + " của bạn đã đặt thành công và đang chờ xác nhận!");

        //Set english content
        notification.setTitle_en("Update order");
        notification.setDesc_en("Order "+ documentId + " has been placed successfully and waiting for merchant confirmation!");

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        time = format.format(Calendar.getInstance().getTime()).toString();

        notification.setTime(time);
        Map<String, String> noti = new HashMap<>();
        noti.put("Title_Vn", notification.getTitle_vn());
        noti.put("Detail_Vn", notification.getDesc_vn());
        noti.put("Title_En", notification.getTitle_en());
        noti.put("Detail_En", notification.getDesc_en());
        noti.put("Date", notification.getTime());
        noti.put("Status", "false");
        root.collection("User/" + userId + "/Notification/")
                .document()
                .set(noti)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
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

    public void getNotificationList(final OnGetDataListener listener) {
        listener.onStart();
        root.collection("User/" + userId + "/Notification")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null)
                        {
                            Log.e(TAG, "onEvent------------------------", error);
                        }
                        if (value != null)
                        {
                            List<DocumentChange> snapshotList = value.getDocumentChanges();

                            for (DocumentChange snapshot : snapshotList)
                            {
                                Log.d(TAG, snapshot.getDocument().getId());
                                for (MyNotification noti : notifications)
                                {
                                    if (snapshot.getDocument().getId().equals(noti.getId())) {
                                        Log.d(TAG, "Deleted");
                                        notifications.remove(noti);
                                        break;
                                    }
                                }

                                MyNotification notification = new MyNotification();
                                notification.setId(snapshot.getDocument().getId());
                                notification.setTitle_vn(snapshot.getDocument().get("Title_Vn").toString());
                                notification.setDesc_vn(snapshot.getDocument().get("Detail_Vn").toString());
                                notification.setTitle_en(snapshot.getDocument().get("Title_En").toString());
                                notification.setDesc_en(snapshot.getDocument().get("Detail_En").toString());
                                notification.setTime(snapshot.getDocument().get("Date").toString());
                                notification.setStatus(snapshot.getDocument().get("Status").toString());

                                notifications.add(notification);
                            }

                            Collections.reverse(notifications);

                            listener.onSuccess();

                            if (NotificationFragment.getAdapter() != null)
                            {
                                NotificationFragment.getAdapter().notifyDataSetChanged();
                            }
                        } else {
                            Log.e(TAG, "onEvent querry was null-----------------");
                        }
                    }
                });

    }

    public void updateNotificationStatus(String id)
    {
        root.collection("User/" + userId + "/Notification/")
                .document(id)
                .update("Status", "true");
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
        voucherList.clear();
        root.collection("User/" + userId + "/Voucher/")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Log.d("GOT", document.getId());
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
        availableVoucherList.clear();
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

    public void setVoucherUsed(String voucherId) {
        root.collection("User/" + userId + "/Voucher/")
                .document(voucherId)
                .update("status", "Đã dùng");
        voucherList.clear();
        getVoucherList();
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

                            Map<String, Object> addressData = (Map<String, Object>) document.get("address");
                            merchant.getAddress().setAddressLine(0, addressData.get("address").toString());
                            merchant.getAddress().setLocality(addressData.get("city").toString());
                            merchant.getAddress().setAdminArea(addressData.get("state").toString());
                            merchant.getAddress().setCountryName(addressData.get("country").toString());
                            merchant.getAddress().setLatitude(Double.parseDouble(addressData.get("latitude").toString()));
                            merchant.getAddress().setLongitude(Double.parseDouble(addressData.get("longitude").toString()));

                            merchant.setName((String) document.get("Name"));
                            merchant.setEmail((String) document.get("Email"));
                            merchant.setPhone((String) document.get("Phone"));
                            merchant.setId(document.getId());

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
                                            product.setSales((String) document.get("Sales"));
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
        searchList = new ArrayList<>();
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

    public void addSearchDataToFirebase(String input) {
        Map<String, String> search = new HashMap<>();
        search.put("SearchString", input);
        root.collection("User/" + userId + "/Search/")
                .document()
                .set(search)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });
    }

    public void querySearch(ArrayList<Product> list, String input, final OnGetDataListener listener) {
        listener.onStart();
        for (Product p : productList) {
            if (p.getName().toLowerCase().contains(input.toLowerCase()) ||
                    p.getEn_Name().toLowerCase().contains(input.toLowerCase())) {
                list.add(p);
            }
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
                                Map<String, Object> addressData = (Map<String, Object>) document.get("address");
                                user.getAddress().setAddressLine(0, addressData.get("address").toString());
                                user.getAddress().setLocality(addressData.get("city").toString());
                                user.getAddress().setAdminArea(addressData.get("state").toString());
                                user.getAddress().setCountryName(addressData.get("country").toString());
                                user.getAddress().setLatitude((Double) addressData.get("latitude"));
                                user.getAddress().setLongitude((Double) addressData.get("longitude"));
                                user.First_Name = document.get("first_Name").toString();
                                user.Last_Name = document.get("last_Name").toString();
                                user.Phone_Number = document.get("phone_Number").toString();

                                StorageReference fileRef = reference.child("UserImage/" + userId);
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
                        Toasty.success(context, "Thêm voucher thành công, vào ví để kiểm tra!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void addNewUser(User user, String uid) {
        Map<String, Object> u = new HashMap<>();
        u.put("first_Name", user.getFirst_Name());
        u.put("last_Name", user.getLast_Name());
        Map<String, Object> addressData = new HashMap<>();
        addressData.put("address", user.getAddress().getAddressLine(0));
        addressData.put("city", user.getAddress().getLocality());
        addressData.put("state", user.getAddress().getAdminArea());
        addressData.put("country", user.getAddress().getCountryName());
        addressData.put("latitude", user.getAddress().getLatitude());
        addressData.put("longitude", user.getAddress().getLongitude());
        u.put("phone_Number", user.getPhone_Number());
        u.put("email", user.getEmail());
        u.put("address", addressData);
        root.collection("User")
                .document(uid)
                .set(u)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

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

    public void updateUserAddress(final OnGetDataListener listener) {
        listener.onStart();

        Map<String, Object> docData = new HashMap<>();

        Map<String, Object> addressData = new HashMap<>();
        addressData.put("address", user.getAddress().getAddressLine(0));
        addressData.put("city", user.getAddress().getLocality());
        addressData.put("state", user.getAddress().getAdminArea());
        addressData.put("country", user.getAddress().getCountryName());
        addressData.put("latitude", user.getAddress().getLatitude());
        addressData.put("longitude", user.getAddress().getLongitude());

        docData.put("address", addressData);

        root.collection("User/")
                .document(userId)
                .update(docData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onSuccess();
                    }
                });
    }

    //region ORDERS
    ArrayList<Map<String, String>> listMap = new ArrayList<>();

    public Product getProductById(String productID) {
        for (Product product : productList) {
            if (product.getId().equals(productID))
                return product;
        }
        return null;
    }

    public void findCommentById(Comment comment, String productId, final OnGetDataListener listener) {
        listener.onStart();
        if (comment.getiD().equals("null")) {
            listener.onSuccess();
            return;
        }
        root.collection("Product/" + productId + "/Comment")
                .document(comment.getiD())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot != null) {
                            Log.e(ORDER_TAG, "Comment: " + comment.getiD() + "/" + productId);
                            comment.setDate(documentSnapshot.get("date").toString());
                            comment.setDetails(documentSnapshot.get("details").toString());
                            comment.setRating(documentSnapshot.get("rating").toString());
                            comment.setUserName(documentSnapshot.get("userName").toString());
                            listener.onSuccess();
                        }
                    }
                });
    }

    public void getOrderById(Orders order) {
        root.collection("User/" + userId + "/Order")
                .document(order.getOrderID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot != null) {

                            boolean check = false;
                            order.status = documentSnapshot.get("status").toString();

                            for (ArrayList<Orders> arrayList : orderList) {
                                for (Orders o : arrayList) {
                                    if (o.getOrderID().equals(order.getOrderID())) {
                                        if (!o.getStatus().equals(order.getStatus())) {
                                            check = true;
                                            arrayList.remove(o);
                                            Log.e("Remove", o.getOrderID());
                                            order.time = documentSnapshot.get("time").toString();
                                            order.discount = Integer.parseInt(documentSnapshot.get("discount").toString());
                                            order.freightCost = Integer.parseInt(documentSnapshot.get("freight_cost").toString());
                                            order.method = documentSnapshot.get("payment_method").toString();
                                            order.status = documentSnapshot.get("status").toString();
                                            order.totalAmount = Integer.parseInt(documentSnapshot.get("total_amount").toString());
                                            order.voucherID = documentSnapshot.get("voucher").toString();
                                            order.user_name = documentSnapshot.get("user_name").toString();
                                            order.user_address = documentSnapshot.get("user_address").toString();
                                            getListOrderedItems(order);
                                            break;
                                        } else
                                            check = true;
                                    }
                                }
                            }

                            Log.e("Check", order.getOrderID() + "/" + check);

                            if (!check) {
                                //    Log.e(ORDER_TAG, "get order details: " + documentSnapshot.getId());
                                order.time = documentSnapshot.get("time").toString();
                                order.discount = Integer.parseInt(documentSnapshot.get("discount").toString());
                                order.freightCost = Integer.parseInt(documentSnapshot.get("freight_cost").toString());
                                order.method = documentSnapshot.get("payment_method").toString();
                                order.status = documentSnapshot.get("status").toString();
                                order.totalAmount = Integer.parseInt(documentSnapshot.get("total_amount").toString());
                                order.voucherID = documentSnapshot.get("voucher").toString();
                                order.user_name = documentSnapshot.get("user_name").toString();
                                order.user_address = documentSnapshot.get("user_address").toString();
                                getListOrderedItems(order);
                            }
                        }
                    }
                });
    }


    public void getListOrdersOfUser() {
        orderList.clear();
        ArrayList<Orders> pendingList = new ArrayList<>();
        ArrayList<Orders> deliveringList = new ArrayList<>();
        ArrayList<Orders> historyList = new ArrayList<>();
        orderList.add(pendingList);
        orderList.add(deliveringList);
        orderList.add(historyList);
        root.collection("User/" + userId + "/Order")
                .orderBy("time", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Log.e(ORDER_TAG, "get id: " + document.getId());
                            Orders order = new Orders();
                            order.setOrderID(document.getId());
                            getOrderById(order);
                        }

                    }
                });
    }

    public void getListOrderedItems(Orders order) {
        ArrayList<OrderItem> orderItemArrayList = new ArrayList<>();

        root.collection("User/" + userId + "/Order/")
                .document(order.getOrderID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot != null) {

                            listMap = (ArrayList<Map<String, String>>) documentSnapshot.get("listItems");

                            for (Map item : listMap) {

                                Log.e(ORDER_TAG, "get order products: " + documentSnapshot.getId() + " " + item.get("product"));

                                Product product = new Product();
                                product = getProductById(item.get("product").toString());

                                OrderItem orderItem = new OrderItem(
                                        order.getOrderID(),
                                        product,
                                        Integer.parseInt(item.get("quantity").toString()),
                                        Integer.parseInt(item.get("price").toString()),
                                        item.get("size").toString()
                                );

                                Comment comment = new Comment();
                                comment.setiD(item.get("comment").toString());

                                findCommentById(comment, product.getId(), new OnGetDataListener() {
                                    @Override
                                    public void onStart() {

                                    }

                                    @Override
                                    public void onSuccess() {
                                        orderItem.setComment(comment);
                                        orderItemArrayList.add(orderItem);
                                        if (item == listMap.get(listMap.size() - 1)) {
                                            order.setListOrderItems(orderItemArrayList);

                                            Log.e("Status", order.getStatus());

                                            if (order.getStatus().equals(OrderStatus.Pending.toString())
                                                    || order.getStatus().equals(OrderStatus.Confirmed.toString())) {
                                                Log.e("Add", order.getOrderID() + order.getStatus());
                                                orderList.get(0).add(order);
                                            }


                                            if (order.getStatus().equals(OrderStatus.Delivering.toString())) {
                                                Log.e("Add", order.getOrderID() + order.getStatus());
                                                orderList.get(1).add(order);
                                            }


                                            if (order.getStatus().equals(OrderStatus.Succeeded.toString())
                                                    || order.getStatus().equals(OrderStatus.Canceled.toString())) {
                                                orderList.get(2).add(order);
                                                Log.e("Add", order.getOrderID() + order.getStatus());
                                            }

                                        }
                                    }
                                });
                            }
                        }
                    }
                });
    }

    public void OrderStatusChange(final OnGetDataListener listener) {
        listener.onStart();
        root.collection("User/" + userId + "/Order/")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("REALTIME", error.getMessage());
                            return;
                        }

                        if (value != null) {
                            List<DocumentChange> documentChangeList = value.getDocumentChanges();
                            for (DocumentChange document : documentChangeList) {
                                Log.e("REALTIME--", document.getDocument().getId());
                                Orders order = new Orders();

                                order.setOrderID(document.getDocument().getId());

                                getOrderById(order);

                                if (document.getDocument().getId().equals(
                                        documentChangeList.get(documentChangeList.size() - 1).getDocument().getId())) {
                                    listener.onSuccess();
                                }
                            }
                        }
                    }
                });

    }

//endregion

//region GET SET

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

    public ArrayList<ArrayList<Orders>> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<ArrayList<Orders>> orderList) {
        this.orderList = orderList;
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
}
