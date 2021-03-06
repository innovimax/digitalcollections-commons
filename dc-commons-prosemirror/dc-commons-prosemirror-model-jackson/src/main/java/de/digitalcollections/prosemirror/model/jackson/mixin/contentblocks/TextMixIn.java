package de.digitalcollections.prosemirror.model.jackson.mixin.contentblocks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.digitalcollections.prosemirror.model.api.Mark;
import de.digitalcollections.prosemirror.model.impl.contentblocks.TextImpl;
import java.util.List;

@JsonDeserialize(as = TextImpl.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface TextMixIn {

  @JsonProperty("marks")
  List<Mark> getMarks();

  @JsonProperty("marks")
  void setMarks(List<Mark> marks);

  @JsonIgnore
  void addMark(Mark mark);

}
