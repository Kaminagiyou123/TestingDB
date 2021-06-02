package com.company.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {
    public static final String DB_NAME = "music.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:/Users/ranyou/Documents/Java_Projects/TestingDB/src/com/company/" + DB_NAME;

    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTISTS = "artist";
    public static final int INDEX_ALBUM_ID = 1;
    public static final int INDEX_ALBUM_NAME = 2;
    public static final int INDEX_ALBUM_ARTISTS = 3;


    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTISTS_ID = "_id";
    public static final String COLUMN_ARTISTS_NAME = "name";
    public static final int INDEX_ARTISTS_ID = 1;
    public static final int INDEX_ARTISTS_NAME = 2;

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_ID = "_id";
    public static final String COLUMN_SONG_TRACK = "track";
    public static final String COLUMN_SONG_TITLE = "title";
    public static final String COLUMN_SONG_ALBUM = "album";

    public static final int INDEX_SONG_ID = 1;
    public static final int INDEX_SONG_TRACK = 2;
    public static final int INDEX_SONG_TITLE = 3;
    public static final int INDEX_SONG_ALBUM = 4;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;
    public static final String QUERY_ALBUMS_BY_ARTIST_START =
            "SELECT " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " FROM " + TABLE_ALBUMS + " " +
                    "INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTISTS + " = " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID
                    + " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + "=\"";

    public static final String QUERY_ALBUMS_ARTIST_SORT =
            "ORDER BY " + TABLE_ALBUMS + "." + COLUMN_ARTISTS_NAME + " COLLATE NOCASE";

    public static final String QUERY_ARTIST_FOR_SONG_START =
            "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + "," +
                    TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + "," +
                    TABLE_SONGS + "." + COLUMN_SONG_TRACK + " FROM " + TABLE_SONGS +
                    " INNER JOIN " + TABLE_ALBUMS + " ON " +
                    TABLE_SONGS + "." + COLUMN_SONG_ALBUM + "=" + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID +
                    " INNER JOIN " + TABLE_ARTISTS + " ON " +
                    TABLE_ARTISTS + "." + COLUMN_ARTISTS_ID + "=" + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTISTS +
                    " WHERE " + TABLE_SONGS + "." + COLUMN_SONG_TITLE + "=\"";

    public static final String QUERY_ARTIST_FOR_SORT_SONG =
            " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTISTS_NAME + "," +
                    TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME +
                    " COLLATE NOCASE";

    public static final String TABLE_ARTIST_SONG_VIEW="artist_list";
    public static final String CREATE_ARTIST_FOR_SONG_VIEW="CREATE VIEW IF NOT EXISTS "+
            TABLE_ARTIST_SONG_VIEW + " AS SELECT "+TABLE_ARTISTS+"."+COLUMN_ARTISTS_NAME+","
            +TABLE_ALBUMS+"."+COLUMN_ALBUM_NAME+", "
            + TABLE_SONGS+"."+COLUMN_SONG_TRACK+","+TABLE_SONGS+"."+COLUMN_SONG_TITLE+
            " FROM "+TABLE_SONGS+
            " INNER JOIN "+TABLE_ALBUMS+ " ON "+TABLE_SONGS+
            "."+COLUMN_SONG_ALBUM+" = "+TABLE_ALBUMS+"."+COLUMN_ALBUM_ID+
            " INNER JOIN "+TABLE_ARTISTS+" ON "+TABLE_ALBUMS+"."+COLUMN_ALBUM_ARTISTS+
            "="+TABLE_ARTISTS+"."+COLUMN_ARTISTS_ID+
            " ORDER BY +" +
            TABLE_ARTISTS+"."+COLUMN_ARTISTS_NAME+", "+
            TABLE_ALBUMS+"."+COLUMN_ALBUM_NAME+","+
            TABLE_SONGS+"."+COLUMN_SONG_TRACK;

    public static final String QUERY_VIEW_SONG_INFO="SELECT * FROM "+TABLE_ARTIST_SONG_VIEW+
            " WHERE "+COLUMN_SONG_TITLE+ " =\"";



    private Connection conn;

    //open database
    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't connect to the database" + e.getMessage());
            return false;
        }
    }

    //close database
    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Couldn't close the database" + e.getMessage());
        }
    }

    //close query artists
    public List<Artist> queryArtists(int sortOrder) throws SQLException {

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_ARTISTS);
        if (sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(COLUMN_ARTISTS_NAME);
            sb.append(" COLLATE NOCASE");
            if (sortOrder == ORDER_BY_DESC) {
                sb.append(" DESC ");
            } else {
                sb.append(" ASC ");
            }
        }

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())
        ) {
            List<Artist> artists = new ArrayList<>();
            while (results.next()) {
                Artist artist = new Artist();
                artist.setId(results.getInt(INDEX_ARTISTS_ID));
                artist.setName(results.getString(INDEX_ARTISTS_NAME));
                artists.add(artist);
            }
            return artists;
        }
    }

    public List<String> queryAlbumsForArtists(String artistsName, int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_ALBUMS_BY_ARTIST_START);
        sb.append(artistsName);
        sb.append("\"");
        if (sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ALBUMS_ARTIST_SORT);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append(" DESC ");
            } else {
                sb.append(" ASC ");
            }
        }

        System.out.println("SQL statement= " + sb.toString());

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {
            List<String> albums = new ArrayList<>();
            while (results.next()) {
                albums.add(results.getString(1));
            }
            return albums;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println(" Query failed: " + throwables.getMessage());

            return null;
        }

    }

    public List<SongArtist> queryArtistForSong(String songName, int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_ARTIST_FOR_SONG_START);
        sb.append(songName);
        sb.append("\"");

        if (sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ARTIST_FOR_SORT_SONG);
            if (sortOrder == ORDER_BY_DESC) {
                sb.append(" DESC ");
            } else {
                sb.append(" ASC ");
            }
        }
            System.out.println("SQL statement= " + sb.toString());
            try (Statement statement = conn.createStatement();
                 ResultSet results = statement.executeQuery(sb.toString()))
            {
                List<SongArtist> songArtists = new ArrayList<>();
                while (results.next()) {
                    SongArtist songArtist = new SongArtist();
                    songArtist.setArtistName(results.getString(1));
                    songArtist.setAlbumName(results.getString(2));
                    songArtist.setTrack(results.getInt(3));
                    songArtists.add(songArtist);

                }
                return songArtists;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                System.out.println(" Query failed: " + throwables.getMessage());
                return null;
            }

        }

        public void querySongsMetadata(){
        String sql="SELECT * FROM "+TABLE_SONGS;
        try(Statement statement=conn.createStatement();
        ResultSet results=statement.executeQuery(sql)){
            ResultSetMetaData meta= results.getMetaData();
            int numColumns=meta.getColumnCount();
            for (int i=1;i<=numColumns;i++){
                System.out.format("Column %d in the songs table is names %s\n",
                        i,meta.getColumnName(i));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        }
        public int getCount(String table){
        String sql="SELECT COUNT(*) AS count FROM "+table;
        try{
            Statement statement= conn.createStatement();
            ResultSet results= statement.executeQuery(sql);{
                int count=results.getInt("count");

                return count;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return -1;
        }
        }

        public boolean createViewForSongArtists(){
        try(Statement statement= conn.createStatement()){
            System.out.println(CREATE_ARTIST_FOR_SONG_VIEW);
            statement.execute(CREATE_ARTIST_FOR_SONG_VIEW);
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
        }
        public List<SongArtist> querySongInfoView(String title){
        StringBuilder sb= new StringBuilder(QUERY_VIEW_SONG_INFO);
        sb.append(title);
        sb.append("\"");
            System.out.println(sb.toString());

            try(Statement statement=conn.createStatement();
            ResultSet results=statement.executeQuery(sb.toString())){
                List<SongArtist> songArtists= new ArrayList<>();
                while (results.next()){
                    SongArtist songArtist=new SongArtist();
                    songArtist.setArtistName(results.getString(1));
                    songArtist.setAlbumName(results.getString(2));
                    songArtist.setTrack(results.getInt(3));
                    songArtists.add(songArtist);
                }
                return songArtists;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return null;
            }
        }
    }







