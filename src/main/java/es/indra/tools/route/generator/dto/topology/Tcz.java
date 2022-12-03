package es.indra.tools.route.generator.dto.topology;

import java.util.LinkedList;
import java.util.List;


public class Tcz {

  private String id;
  
  private String name;
  
  private List<Track> tracks;
  
  public Tcz() {
    this.tracks = new LinkedList<>();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Track> getTracks() {
    return tracks;
  }

  public void setTracks(List<Track> tracks) {
    this.tracks = tracks;
  }

  public void addTrack(Track track) {
    this.tracks.add(track);
  }
  
}
