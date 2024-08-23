import com.fasterxml.jackson.annotation.JsonProperty

case class HistDto(
  @JsonProperty("hist_film") histFilm: Seq[Int],
  @JsonProperty("hist_all") histAll: Seq[Int],
)
