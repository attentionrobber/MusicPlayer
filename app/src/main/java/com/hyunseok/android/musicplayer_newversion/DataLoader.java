package com.hyunseok.android.musicplayer_newversion;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.hyunseok.android.musicplayer_newversion.domain.Album;
import com.hyunseok.android.musicplayer_newversion.domain.Artist;
import com.hyunseok.android.musicplayer_newversion.domain.Favorite;
import com.hyunseok.android.musicplayer_newversion.domain.Folder;
import com.hyunseok.android.musicplayer_newversion.domain.Music;
import com.hyunseok.android.musicplayer_newversion.util.Logger.Logger;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-02-01.
 */

public class DataLoader {

    private static List<Music> datas = new ArrayList<>(); // SOLID 를 적용 선언부를 ArrayList -> List로 바꿈
    private static List<Favorite> favoriteDatas = new ArrayList<>();
    private static List<Folder> folderDatas = new ArrayList<>();
    private static List<Album> albumDatas = new ArrayList<>();

    // 일반적으로 데이터(ArrayList<Music> datas)를 public static으로 사용하기보다는
    // get()함수를 public static으로 사용해서 다른 클래스끼리 공유한다.
    // datas를 두 개의 Activity에서 공유하기 위해 static으로 변경. ==>> 생성자 함수가 필요가없어짐.
    public static List<Favorite> getFavorite(Context context) {
        if(favoriteDatas == null || favoriteDatas.size() == 0) {
            loadMusic(context);
            //loadFavorite(context);
        }
        return favoriteDatas;
    }
    public static List<Music> getMusic(Context context) {
        if(datas == null || datas.size() == 0) {
            loadMusic(context);
        }
        return datas;
    }
    public static List<Folder> getFolder(Context context) {
        if(folderDatas == null || folderDatas.size() == 0) {
            loadMusic(context);
            //loadFolder(context);
        }
        return folderDatas;
    }
    public static List<Album> getAlbum(Context context) {
        if(albumDatas == null || albumDatas.size() == 0) {
            loadMusic(context);
            //loadAlbum(context);
        }
        return albumDatas;
    }

    public static void loadMusic(Context context) {

        // 1. 데이터 컨텐츠 URI정의
        // 데이터URI(음악) : MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        final Uri URI_MUSIC = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        // 2. 데이터에서 가져올 데이터 컬럼명을 projections에 담는다.
        // 데이터 컬럼명은 Content URI의 패키지에 들어있다. MediaStore.Audio.Media. 에서 찾으면 됨.
        final String PROJ[] = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ARTIST_KEY,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.IS_MUSIC,
                MediaStore.Audio.Media.COMPOSER,
                MediaStore.Audio.Media.YEAR
        };

        // 1. 데이터에 접근하기 위해 Content Resolver를 불러온다.
        ContentResolver resolver = context.getContentResolver();
        // 3. Content Resolver로 쿼리한 데이터를 cursor에 담게된다.
        Cursor cursor = resolver.query(URI_MUSIC, PROJ, null, null, null);

        if(cursor != null) {
            // 4. cursor에 담긴 데이터를 반복문을 돌면서 꺼내고 datas에 담아준다.
            while( cursor.moveToNext() ) {

                Music music = new Music();

                // 5. 커서의 컬럼 인덱스를 가져온 후 컬럼인덱스에 해당하는 proj을 세팅
                music.id           = getInt(   cursor, PROJ[0]);
                music.album_id     = getInt(   cursor, PROJ[1]);
                music.title        = getString(cursor, PROJ[2]);
                music.artist_id    = getInt(   cursor, PROJ[3]);
                music.artist       = getString(cursor, PROJ[4]);
                music.artist_key   = getString(cursor, PROJ[5]);
                music.duration     = getInt(   cursor, PROJ[6]);
                music.is_music     = getString(cursor, PROJ[7]);
                music.composer     = getString(cursor, PROJ[8]);
                music.year         = getString(cursor, PROJ[9]);

                music.music_uri = getMusicUri(music.id);
                music.album_img_uri = Uri.parse("content://media/external/audio/albumart/" + music.album_id);
                //music.album_img = getAlbumImageSimple(music.album_id); // URI로 직접 이미지를 로드한다. (이미지 못불러오는 경우 있음)
                //music.bitmap_img = getAlbumImageBitmap(music.album_id, context); // Bitmap으로 처리해서 이미지를 로드한다. (매우느림)

                datas.add(music);
            }
            // 6. 사용 후 close를 해주지 않으면 메모리 누수가 발생할 수 있다.
            cursor.close();
        }
    }


    public static void loadArtist(Context context) {

        final Uri URI_ARTIST = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

        final String PROJ[] = {
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
                MediaStore.Audio.Artists.ARTIST_KEY
        };

        // 1. 데이터에 접근하기 위해 Content Resolver를 불러온다.
        ContentResolver resolver = context.getContentResolver();
        // 3. Content Resolver로 쿼리한 데이터를 cursor에 담게된다.
        Cursor cursor = resolver.query(URI_ARTIST, PROJ, null, null, null);

        if(cursor != null) {
            // 4. cursor에 담긴 데이터를 반복문을 돌면서 꺼내고 datas에 담아준다.
            while( cursor.moveToNext() ) {

                Artist artist = new Artist();

                // 5. 커서의 컬럼 인덱스를 가져온 후 컬럼인덱스에 해당하는 proj을 세팅
                artist.id = getInt(cursor, PROJ[0]);
                artist.artist = getString(cursor, PROJ[1]);
                artist.artist_key = getString(cursor, PROJ[2]);
                artist.number_of_albums = getInt(cursor, PROJ[3]);
                artist.number_of_tracks = getInt(cursor, PROJ[4]);

                artist.album_id = getAlbumIdByArtistId(artist.id); // MusicDatas로 album id 를 구한다.
                artist.album_image_uri = getAlbumUriByArtistId(artist.id); // MusicDatas로 album image uri 구한다.

                //artistDatas.add(artist);
            }
            // 6. 사용 후 close를 해주지 않으면 메모리 누수가 발생할 수 있다.
            cursor.close();
        }
    }

    private static int getAlbumIdByArtistId(int artist_id) {
        for(Music music : datas) {
            if(music.artist_id == artist_id) {
                return music.album_id;
            }
        }
        return -1;
    }

    public static Uri getAlbumUriByArtistId(int artist_id) {
        for(Music music : datas) {
            if(music.artist_id == artist_id) {
                return music.album_img_uri;
            }
        }
        return null;
    }

    private static String getString(Cursor cursor, String columnName){
        int idx = cursor.getColumnIndex(columnName);
        return cursor.getString(idx);
    }

    private static int getInt(Cursor cursor, String columnName){
        int idx = cursor.getColumnIndex(columnName);
        return cursor.getInt(idx);
    }

    // 음악id로 Uri를 가져오는 함수
    private static Uri getMusicUri(int music_id) {
        Uri content_uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        return Uri.withAppendedPath(content_uri, music_id+""); // Uri 주소를 합쳐주는 함수
    }

    // 음악의 장르를 함수로 받아온다.
    // -> MediaStore.Audio.Media. 에 정의된 상수가 없기 때문에...
    private static String getGenre() {

        return "";
    }


    /**
     * 가장 간단하게 앨범이미지를 가져오는 방법
     * 문제점 : 실제 앨범데이터만 있어서 이미지를 불러오지 못하는 경우가 있다.
     * @param album_id
     * @return
     */
    @Deprecated
    private Uri getAlbumImageSimple(String album_id) {
        return Uri.parse("content://media/external/audio/albumart/" + album_id);
    }

    /**
     * Content Resolver를 통해 가져옴
     * @param album_id
     * @return
     */
    @Deprecated
    private static Bitmap getAlbumImageBitmap(String album_id, Context context) {
        // 1. 앨범아이디로 URI생성
        Uri uri = Uri.parse("content://media/external/audio/albumart/" + album_id);
        // 2. ContentResolver 가져오기
        ContentResolver resolver = context.getContentResolver();

        try {
            // 3. resolver에서 Stream열기
            InputStream is = resolver.openInputStream(uri);
            // 4. BitmapFactory를 통해 이미지 데이터를 가져온다.
            Bitmap img = BitmapFactory.decodeStream(is); // Bitmap이미지를 Stream의 형태로 가져오면 Decode를 해줘야한다.

            return img;
        } catch (FileNotFoundException e) {
            Logger.print(e.toString(), "getAlbumImageBitmap");
            e.printStackTrace(); // 동일 스레드가 아닌 다른 스레드에서 실행되므로 시스템에 영향을 미치지 않는다. Sysout은 시스템 성능에 영향을미침.
        }
        return null;
    }
}
