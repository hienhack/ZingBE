package hcmus.zingmp3.common.domain.aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.internal.build.AllowNonPortable;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Aggregate {
    private UUID id;
}
