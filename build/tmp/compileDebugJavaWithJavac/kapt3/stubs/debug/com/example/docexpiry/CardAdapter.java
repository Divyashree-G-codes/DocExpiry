package com.example.docexpiry;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 \u001a2\u0012\u0012\u0004\u0012\u00020\u0002\u0012\b\u0012\u00060\u0003R\u00020\u00000\u00012\u00020\u0004:\u0003\u0019\u001a\u001bB\u001f\u0012\u0018\u0010\u0005\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006\u00a2\u0006\u0002\u0010\tJ\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00020\u000bJ\b\u0010\r\u001a\u00020\u000eH\u0016J\u001c\u0010\u000f\u001a\u00020\b2\n\u0010\u0010\u001a\u00060\u0003R\u00020\u00002\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u001c\u0010\u0013\u001a\u00060\u0003R\u00020\u00002\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0012H\u0016J\u0018\u0010\u0017\u001a\u00020\b2\u000e\u0010\u0018\u001a\n\u0012\u0004\u0012\u00020\u0002\u0018\u00010\u000bH\u0016R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R \u0010\u0005\u001a\u0014\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/example/docexpiry/CardAdapter;", "Landroidx/recyclerview/widget/ListAdapter;", "Lcom/example/docexpiry/data/Card;", "Lcom/example/docexpiry/CardAdapter$VH;", "Landroid/widget/Filterable;", "onAction", "Lkotlin/Function2;", "Lcom/example/docexpiry/CardAdapter$Action;", "", "(Lkotlin/jvm/functions/Function2;)V", "fullList", "", "currentFullList", "getFilter", "Landroid/widget/Filter;", "onBindViewHolder", "holder", "position", "", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "submitList", "list", "Action", "Companion", "VH", "app_debug"})
public final class CardAdapter extends androidx.recyclerview.widget.ListAdapter<com.example.docexpiry.data.Card, com.example.docexpiry.CardAdapter.VH> implements android.widget.Filterable {
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function2<com.example.docexpiry.data.Card, com.example.docexpiry.CardAdapter.Action, kotlin.Unit> onAction = null;
    @org.jetbrains.annotations.NotNull
    private java.util.List<com.example.docexpiry.data.Card> fullList;
    @org.jetbrains.annotations.NotNull
    private static final androidx.recyclerview.widget.DiffUtil.ItemCallback<com.example.docexpiry.data.Card> DIFF = null;
    @org.jetbrains.annotations.NotNull
    public static final com.example.docexpiry.CardAdapter.Companion Companion = null;
    
    public CardAdapter(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super com.example.docexpiry.data.Card, ? super com.example.docexpiry.CardAdapter.Action, kotlin.Unit> onAction) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.example.docexpiry.data.Card> currentFullList() {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public com.example.docexpiry.CardAdapter.VH onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.example.docexpiry.CardAdapter.VH holder, int position) {
    }
    
    @java.lang.Override
    public void submitList(@org.jetbrains.annotations.Nullable
    java.util.List<com.example.docexpiry.data.Card> list) {
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public android.widget.Filter getFilter() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007\u00a8\u0006\b"}, d2 = {"Lcom/example/docexpiry/CardAdapter$Action;", "", "(Ljava/lang/String;I)V", "VIEW", "EDIT", "DELETE", "SHARE", "VIEW_IMAGE", "app_debug"})
    public static enum Action {
        /*public static final*/ VIEW /* = new VIEW() */,
        /*public static final*/ EDIT /* = new EDIT() */,
        /*public static final*/ DELETE /* = new DELETE() */,
        /*public static final*/ SHARE /* = new SHARE() */,
        /*public static final*/ VIEW_IMAGE /* = new VIEW_IMAGE() */;
        
        Action() {
        }
        
        @org.jetbrains.annotations.NotNull
        public static kotlin.enums.EnumEntries<com.example.docexpiry.CardAdapter.Action> getEntries() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\b"}, d2 = {"Lcom/example/docexpiry/CardAdapter$Companion;", "", "()V", "DIFF", "Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "Lcom/example/docexpiry/data/Card;", "getDIFF", "()Landroidx/recyclerview/widget/DiffUtil$ItemCallback;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final androidx.recyclerview.widget.DiffUtil.ItemCallback<com.example.docexpiry.data.Card> getDIFF() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/example/docexpiry/CardAdapter$VH;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "b", "Lcom/example/docexpiry/databinding/ItemCardBinding;", "(Lcom/example/docexpiry/CardAdapter;Lcom/example/docexpiry/databinding/ItemCardBinding;)V", "sdf", "Ljava/text/SimpleDateFormat;", "bind", "", "card", "Lcom/example/docexpiry/data/Card;", "app_debug"})
    public final class VH extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final com.example.docexpiry.databinding.ItemCardBinding b = null;
        @org.jetbrains.annotations.NotNull
        private final java.text.SimpleDateFormat sdf = null;
        
        public VH(@org.jetbrains.annotations.NotNull
        com.example.docexpiry.databinding.ItemCardBinding b) {
            super(null);
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull
        com.example.docexpiry.data.Card card) {
        }
    }
}