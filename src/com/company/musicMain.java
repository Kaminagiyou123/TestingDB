package com.company;

import com.company.model.Artist;
import com.company.model.Datasource;
import com.company.model.SongArtist;

import java.sql.SQLException;
import java.util.List;

public class musicMain {
    public static void main(String[] args) throws SQLException {
        Datasource datasource=new Datasource();
        if (!datasource.open()){
            System.out.println("Can't open datasource");
            return;
        }
        List<Artist> artists= datasource.queryArtists(Datasource.ORDER_BY_NONE);
        if (artists==null){
            System.out.println("No artists");
            return;
        }
        for (Artist a:artists){
            System.out.println("ID= "+a.getId()+ ",name= "+a.getName());
        }

        List<String> albumsForArtist=
                datasource.queryAlbumsForArtists("Carole King", datasource.ORDER_BY_DESC);
        for (String album: albumsForArtist){
            System.out.println(album);
        }

        List<SongArtist> songArtists=datasource.queryArtistForSong("Go Your Own Way",Datasource.ORDER_BY_DESC);
        if (songArtists==null){
            System.out.println("Couldnt find the artist for the song");
            return;
        } for (SongArtist artist :songArtists) {
            System.out.println(" Artist name= " + artist.getArtistName() +
                    " Album name = " + artist.getAlbumName() +
                    " Track= " + artist.getTrack());
        }
        datasource.querySongsMetadata();
        int count=datasource.getCount(Datasource.TABLE_SONGS);
        System.out.println("Number of songs is "+count);
        datasource.createViewForSongArtists();

        songArtists= datasource.querySongInfoView("Heartless");

        if (songArtists.isEmpty()){
            System.out.println("Couldn't find the artist for the song");
            return;
        }
        for(SongArtist a:songArtists){
            System.out.println(" FROM VIEW= "+a.getArtistName()
            +" "+a.getAlbumName()+ " "+a.getTrack());
        }

        datasource.close();
    }
}
