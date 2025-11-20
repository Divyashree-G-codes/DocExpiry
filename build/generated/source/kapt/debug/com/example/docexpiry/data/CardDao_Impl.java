package com.example.docexpiry.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@SuppressWarnings({"unchecked", "deprecation"})
public final class CardDao_Impl implements CardDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Card> __insertionAdapterOfCard;

  private final EntityDeletionOrUpdateAdapter<Card> __deletionAdapterOfCard;

  private final EntityDeletionOrUpdateAdapter<Card> __updateAdapterOfCard;

  public CardDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCard = new EntityInsertionAdapter<Card>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `cards` (`id`,`type`,`number`,`ownerName`,`ownerAddress`,`ownerDob`,`authority`,`issuedDate`,`expiryDate`,`photoUri`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Card entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getType() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getType());
        }
        if (entity.getNumber() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNumber());
        }
        if (entity.getOwnerName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getOwnerName());
        }
        if (entity.getOwnerAddress() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getOwnerAddress());
        }
        statement.bindLong(6, entity.getOwnerDob());
        if (entity.getAuthority() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getAuthority());
        }
        statement.bindLong(8, entity.getIssuedDate());
        statement.bindLong(9, entity.getExpiryDate());
        if (entity.getPhotoUri() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getPhotoUri());
        }
      }
    };
    this.__deletionAdapterOfCard = new EntityDeletionOrUpdateAdapter<Card>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `cards` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Card entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfCard = new EntityDeletionOrUpdateAdapter<Card>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `cards` SET `id` = ?,`type` = ?,`number` = ?,`ownerName` = ?,`ownerAddress` = ?,`ownerDob` = ?,`authority` = ?,`issuedDate` = ?,`expiryDate` = ?,`photoUri` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Card entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getType() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getType());
        }
        if (entity.getNumber() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNumber());
        }
        if (entity.getOwnerName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getOwnerName());
        }
        if (entity.getOwnerAddress() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getOwnerAddress());
        }
        statement.bindLong(6, entity.getOwnerDob());
        if (entity.getAuthority() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getAuthority());
        }
        statement.bindLong(8, entity.getIssuedDate());
        statement.bindLong(9, entity.getExpiryDate());
        if (entity.getPhotoUri() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getPhotoUri());
        }
        statement.bindLong(11, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final Card card, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCard.insertAndReturnId(card);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final Card card, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfCard.handle(card);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Card card, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCard.handle(card);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<Card>> getAll() {
    final String _sql = "SELECT * FROM cards ORDER BY expiryDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"cards"}, false, new Callable<List<Card>>() {
      @Override
      @Nullable
      public List<Card> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "number");
          final int _cursorIndexOfOwnerName = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerName");
          final int _cursorIndexOfOwnerAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerAddress");
          final int _cursorIndexOfOwnerDob = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerDob");
          final int _cursorIndexOfAuthority = CursorUtil.getColumnIndexOrThrow(_cursor, "authority");
          final int _cursorIndexOfIssuedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "issuedDate");
          final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryDate");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final List<Card> _result = new ArrayList<Card>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Card _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpNumber;
            if (_cursor.isNull(_cursorIndexOfNumber)) {
              _tmpNumber = null;
            } else {
              _tmpNumber = _cursor.getString(_cursorIndexOfNumber);
            }
            final String _tmpOwnerName;
            if (_cursor.isNull(_cursorIndexOfOwnerName)) {
              _tmpOwnerName = null;
            } else {
              _tmpOwnerName = _cursor.getString(_cursorIndexOfOwnerName);
            }
            final String _tmpOwnerAddress;
            if (_cursor.isNull(_cursorIndexOfOwnerAddress)) {
              _tmpOwnerAddress = null;
            } else {
              _tmpOwnerAddress = _cursor.getString(_cursorIndexOfOwnerAddress);
            }
            final long _tmpOwnerDob;
            _tmpOwnerDob = _cursor.getLong(_cursorIndexOfOwnerDob);
            final String _tmpAuthority;
            if (_cursor.isNull(_cursorIndexOfAuthority)) {
              _tmpAuthority = null;
            } else {
              _tmpAuthority = _cursor.getString(_cursorIndexOfAuthority);
            }
            final long _tmpIssuedDate;
            _tmpIssuedDate = _cursor.getLong(_cursorIndexOfIssuedDate);
            final long _tmpExpiryDate;
            _tmpExpiryDate = _cursor.getLong(_cursorIndexOfExpiryDate);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            _item = new Card(_tmpId,_tmpType,_tmpNumber,_tmpOwnerName,_tmpOwnerAddress,_tmpOwnerDob,_tmpAuthority,_tmpIssuedDate,_tmpExpiryDate,_tmpPhotoUri);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAllSync(final Continuation<? super List<Card>> $completion) {
    final String _sql = "SELECT * FROM cards";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Card>>() {
      @Override
      @Nullable
      public List<Card> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "number");
          final int _cursorIndexOfOwnerName = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerName");
          final int _cursorIndexOfOwnerAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerAddress");
          final int _cursorIndexOfOwnerDob = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerDob");
          final int _cursorIndexOfAuthority = CursorUtil.getColumnIndexOrThrow(_cursor, "authority");
          final int _cursorIndexOfIssuedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "issuedDate");
          final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryDate");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final List<Card> _result = new ArrayList<Card>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Card _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpNumber;
            if (_cursor.isNull(_cursorIndexOfNumber)) {
              _tmpNumber = null;
            } else {
              _tmpNumber = _cursor.getString(_cursorIndexOfNumber);
            }
            final String _tmpOwnerName;
            if (_cursor.isNull(_cursorIndexOfOwnerName)) {
              _tmpOwnerName = null;
            } else {
              _tmpOwnerName = _cursor.getString(_cursorIndexOfOwnerName);
            }
            final String _tmpOwnerAddress;
            if (_cursor.isNull(_cursorIndexOfOwnerAddress)) {
              _tmpOwnerAddress = null;
            } else {
              _tmpOwnerAddress = _cursor.getString(_cursorIndexOfOwnerAddress);
            }
            final long _tmpOwnerDob;
            _tmpOwnerDob = _cursor.getLong(_cursorIndexOfOwnerDob);
            final String _tmpAuthority;
            if (_cursor.isNull(_cursorIndexOfAuthority)) {
              _tmpAuthority = null;
            } else {
              _tmpAuthority = _cursor.getString(_cursorIndexOfAuthority);
            }
            final long _tmpIssuedDate;
            _tmpIssuedDate = _cursor.getLong(_cursorIndexOfIssuedDate);
            final long _tmpExpiryDate;
            _tmpExpiryDate = _cursor.getLong(_cursorIndexOfExpiryDate);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            _item = new Card(_tmpId,_tmpType,_tmpNumber,_tmpOwnerName,_tmpOwnerAddress,_tmpOwnerDob,_tmpAuthority,_tmpIssuedDate,_tmpExpiryDate,_tmpPhotoUri);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getById(final long id, final Continuation<? super Card> $completion) {
    final String _sql = "SELECT * FROM cards WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Card>() {
      @Override
      @Nullable
      public Card call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "number");
          final int _cursorIndexOfOwnerName = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerName");
          final int _cursorIndexOfOwnerAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerAddress");
          final int _cursorIndexOfOwnerDob = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerDob");
          final int _cursorIndexOfAuthority = CursorUtil.getColumnIndexOrThrow(_cursor, "authority");
          final int _cursorIndexOfIssuedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "issuedDate");
          final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryDate");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final Card _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpNumber;
            if (_cursor.isNull(_cursorIndexOfNumber)) {
              _tmpNumber = null;
            } else {
              _tmpNumber = _cursor.getString(_cursorIndexOfNumber);
            }
            final String _tmpOwnerName;
            if (_cursor.isNull(_cursorIndexOfOwnerName)) {
              _tmpOwnerName = null;
            } else {
              _tmpOwnerName = _cursor.getString(_cursorIndexOfOwnerName);
            }
            final String _tmpOwnerAddress;
            if (_cursor.isNull(_cursorIndexOfOwnerAddress)) {
              _tmpOwnerAddress = null;
            } else {
              _tmpOwnerAddress = _cursor.getString(_cursorIndexOfOwnerAddress);
            }
            final long _tmpOwnerDob;
            _tmpOwnerDob = _cursor.getLong(_cursorIndexOfOwnerDob);
            final String _tmpAuthority;
            if (_cursor.isNull(_cursorIndexOfAuthority)) {
              _tmpAuthority = null;
            } else {
              _tmpAuthority = _cursor.getString(_cursorIndexOfAuthority);
            }
            final long _tmpIssuedDate;
            _tmpIssuedDate = _cursor.getLong(_cursorIndexOfIssuedDate);
            final long _tmpExpiryDate;
            _tmpExpiryDate = _cursor.getLong(_cursorIndexOfExpiryDate);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            _result = new Card(_tmpId,_tmpType,_tmpNumber,_tmpOwnerName,_tmpOwnerAddress,_tmpOwnerDob,_tmpAuthority,_tmpIssuedDate,_tmpExpiryDate,_tmpPhotoUri);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getCardById(final long id, final Continuation<? super Card> $completion) {
    final String _sql = "SELECT * FROM cards WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Card>() {
      @Override
      @Nullable
      public Card call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "number");
          final int _cursorIndexOfOwnerName = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerName");
          final int _cursorIndexOfOwnerAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerAddress");
          final int _cursorIndexOfOwnerDob = CursorUtil.getColumnIndexOrThrow(_cursor, "ownerDob");
          final int _cursorIndexOfAuthority = CursorUtil.getColumnIndexOrThrow(_cursor, "authority");
          final int _cursorIndexOfIssuedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "issuedDate");
          final int _cursorIndexOfExpiryDate = CursorUtil.getColumnIndexOrThrow(_cursor, "expiryDate");
          final int _cursorIndexOfPhotoUri = CursorUtil.getColumnIndexOrThrow(_cursor, "photoUri");
          final Card _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpNumber;
            if (_cursor.isNull(_cursorIndexOfNumber)) {
              _tmpNumber = null;
            } else {
              _tmpNumber = _cursor.getString(_cursorIndexOfNumber);
            }
            final String _tmpOwnerName;
            if (_cursor.isNull(_cursorIndexOfOwnerName)) {
              _tmpOwnerName = null;
            } else {
              _tmpOwnerName = _cursor.getString(_cursorIndexOfOwnerName);
            }
            final String _tmpOwnerAddress;
            if (_cursor.isNull(_cursorIndexOfOwnerAddress)) {
              _tmpOwnerAddress = null;
            } else {
              _tmpOwnerAddress = _cursor.getString(_cursorIndexOfOwnerAddress);
            }
            final long _tmpOwnerDob;
            _tmpOwnerDob = _cursor.getLong(_cursorIndexOfOwnerDob);
            final String _tmpAuthority;
            if (_cursor.isNull(_cursorIndexOfAuthority)) {
              _tmpAuthority = null;
            } else {
              _tmpAuthority = _cursor.getString(_cursorIndexOfAuthority);
            }
            final long _tmpIssuedDate;
            _tmpIssuedDate = _cursor.getLong(_cursorIndexOfIssuedDate);
            final long _tmpExpiryDate;
            _tmpExpiryDate = _cursor.getLong(_cursorIndexOfExpiryDate);
            final String _tmpPhotoUri;
            if (_cursor.isNull(_cursorIndexOfPhotoUri)) {
              _tmpPhotoUri = null;
            } else {
              _tmpPhotoUri = _cursor.getString(_cursorIndexOfPhotoUri);
            }
            _result = new Card(_tmpId,_tmpType,_tmpNumber,_tmpOwnerName,_tmpOwnerAddress,_tmpOwnerDob,_tmpAuthority,_tmpIssuedDate,_tmpExpiryDate,_tmpPhotoUri);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
