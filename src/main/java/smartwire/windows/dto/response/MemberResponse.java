package smartwire.windows.dto.response;

import lombok.Getter;
import smartwire.windows.domain.Member;

@Getter
public class MemberResponse extends ApiResponse {
    private Member body;
}
