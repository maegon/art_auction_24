package com.example.ArtAuction_24;

import com.example.ArtAuction_24.domain.answer.service.AnswerService;
import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.artist.service.ArtistService;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.service.MemberService;
import com.example.ArtAuction_24.domain.product.service.ProductService;
import com.example.ArtAuction_24.domain.question.entity.Question;
import com.example.ArtAuction_24.domain.question.entity.QuestionType;
import com.example.ArtAuction_24.domain.question.form.QuestionForm;
import com.example.ArtAuction_24.domain.question.repository.QuestionRepository;
import com.example.ArtAuction_24.domain.question.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
class ArtAuction24ApplicationTests {

	@Autowired
	private AnswerService answerService;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private ArtistService artistService;

	@Autowired
	private ProductService productService;

	@Test
	void testJpa() {
		String password = "test123!";

		memberService.join("", "admin", password, "admin@test.com", "admin",
				"010-1234-1234", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층");

		Member M1 = memberService.join("", "user1", password, "minseopkim99@gmail.com", "user1",
				"010-1234-5678", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층");

		memberService.join("", "dlagorhs99", password, "dlagorhs99@naver.com", "나다",
				"010-1314-5838", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층");

		memberService.join("", "user3", password, "bok06023@naver.com", "user3",
				"010-1314-5587", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층");

		memberService.join("", "user4", password, "user4@test.com", "user4",
				"010-1234-5678", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층");

		memberService.join("", "artist", password, "artist1@test.com", "artist1",
				"010-1314-4654", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층");

		memberService.join("", "artist2", password, "artist2@test.com", "artist2",
				"010-1314-4654", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층");

		memberService.join("", "artist3", password, "artist3@test.com", "artist3",
				"010-1314-4654", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층");


		memberService.join("", "artist4", password, "artist4@test.com", "artist4",
				"010-1314-4654", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층");

		memberService.join("", "artist5", password, "artist5@test.com", "artist5",
				"010-1314-4654", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층");

		memberService.join("", "artist6", password, "artist6@test.com", "artist6",
				"010-1314-4654", "대전광역시 서구 대덕대로 179 굿모닝어학원빌딩 9층");


		artistService.create("김작가", "kimArtist", "1950-06-28", "안녕! 나 김작가입니다. 제 작품은 일상의 아름다움을 포착하며, 각기 다른 색과 형태를 통해 감정을 전달하려고 합니다. 제 작업은 항상 새로운 시도를 통해 관객들에게 신선한 경험을 선사하는 데 중점을 두고 있습니다.", "SBS 아트협회 임원");
		artistService.create("나작가", "naArtist", "1968-12-08", "안녕! 나 나작가입니다. 제 작품은 일상의 아름다움을 포착하며, 각기 다른 색과 형태를 통해 감정을 전달하려고 합니다. 제 작업은 항상 새로운 시도를 통해 관객들에게 신선한 경험을 선사하는 데 중점을 두고 있습니다.", "SBS 아트협회 임원");
		artistService.create("박작가", "parkArtist", "1999-04-03", "안녕! 나 박작가입니다. 제 작품은 일상의 아름다움을 포착하며, 각기 다른 색과 형태를 통해 감정을 전달하려고 합니다. 제 작업은 항상 새로운 시도를 통해 관객들에게 신선한 경험을 선사하는 데 중점을 두고 있습니다.", "SBS 아트협회 임원");
		artistService.create("이작가", "leeArtist", "1985-11-02", "안녕! 나 이작가입니다. 제 작품은 일상의 아름다움을 포착하며, 각기 다른 색과 형태를 통해 감정을 전달하려고 합니다. 제 작업은 항상 새로운 시도를 통해 관객들에게 신선한 경험을 선사하는 데 중점을 두고 있습니다.", "SBS 아트협회 임원");
		artistService.create("최작가", "choiArtist", "1975-01-15",  "안녕! 나 최작가입니다. 제 작품은 일상의 아름다움을 포착하며, 각기 다른 색과 형태를 통해 감정을 전달하려고 합니다. 제 작업은 항상 새로운 시도를 통해 관객들에게 신선한 경험을 선사하는 데 중점을 두고 있습니다.", "SBS 아트협회 임원");
		artistService.create("정작가", "jungArtist", "1980-07-19",  "안녕! 나 정작가입니다. 제 작품은 일상의 아름다움을 포착하며, 각기 다른 색과 형태를 통해 감정을 전달하려고 합니다. 제 작업은 항상 새로운 시도를 통해 관객들에게 신선한 경험을 선사하는 데 중점을 두고 있습니다.", "SBS 아트협회 임원");
		artistService.create("강작가", "kangArtist", "1992-03-25",  "안녕! 나 강작가입니다. 제 작품은 일상의 아름다움을 포착하며, 각기 다른 색과 형태를 통해 감정을 전달하려고 합니다. 제 작업은 항상 새로운 시도를 통해 관객들에게 신선한 경험을 선사하는 데 중점을 두고 있습니다.", "SBS 아트협회 임원");
		artistService.create("한작가", "hanArtist", "2001-09-17", "안녕! 나 한작가입니다. 제 작품은 일상의 아름다움을 포착하며, 각기 다른 색과 형태를 통해 감정을 전달하려고 합니다. 제 작업은 항상 새로운 시도를 통해 관객들에게 신선한 경험을 선사하는 데 중점을 두고 있습니다.", "SBS 아트협회 임원");
		artistService.create("어작가", "uhArtist", "1999-09-17", "안녕! 나 어작가입니다. 제 작품은 일상의 아름다움을 포착하며, 각기 다른 색과 형태를 통해 감정을 전달하려고 합니다. 제 작업은 항상 새로운 시도를 통해 관객들에게 신선한 경험을 선사하는 데 중점을 두고 있습니다.", "SBS 아트협회 임원");


		Artist kim = artistService.getArtistByKorName("김작가");
		Artist na = artistService.getArtistByKorName("나작가");
		Artist park = artistService.getArtistByKorName("박작가");
		Artist lee = artistService.getArtistByKorName("이작가");





		productService.create("토끼", "이 그림은 부드러운 색조와 섬세한 디테일로 그려진 귀여운 토끼를 담고 있습니다. 토끼의 큰 눈과 푹신한 털은 따뜻하고 아늑한 느낌을 주며, 자연 속에서 조용히 쉬고 있는 모습을 표현했습니다. 그림 속 토끼는 순수하고 순진한 매력을 지니고 있어, 보는 이로 하여금 평화롭고 행복한 감정을 느끼게 합니다. 이 작품은 자연과 동물의 아름다움을 경이롭게 담아내려는 저의 노력의 결과입니다.", "색연필", "90×128cm", new BigDecimal(10000), LocalDateTime.now(), "/image/토끼.jpg", "수채화", kim);
		productService.create("은하계", "이 그림은 광활한 우주를 배경으로 한 은하계를 담아낸 작품입니다. 별들이 수놓은 밤하늘과 그 사이를 흐르는 은하의 신비로운 모습이 아름답게 표현되어 있습니다. 색다른 색조와 반짝이는 별빛, 그리고 깊은 우주의 신비로움이 조화를 이루며 관객을 우주의 끝없는 광활함 속으로 초대합니다. 이 작품은 우주의 웅장함과 아름다움을 탐구하고, 무한한 상상의 세계를 경험하게 해주는 저의 창의력의 표현입니다..", "종이에 물감", "600x300cm", new BigDecimal(11000), LocalDateTime.now(), "/image/은하계.jpg", "수채화", na);
		productService.create("천사와 악마", "이 그림은 천사와 악마의 대비를 통해 인간 내면의 갈등과 도덕적 이중성을 탐구한 작품입니다. 천사는 순수함과 희망의 상징으로, 부드럽고 따뜻한 색조로 표현되어 있습니다. 반면, 악마는 어두운 색상과 날카로운 선들로 그려져 있으며, 위협적이고 강렬한 존재감을 드러냅니다. 두 캐릭터의 극명한 대조는 선과 악, 빛과 어둠 간의 끊임없는 싸움을 시각적으로 드라마틱하게 담아내며, 인간의 도덕적 선택과 그에 따른 내적 갈등을 관객에게 깊이 있게 전달합니다.", "종이에 물감", "30x20cm", new BigDecimal(12000), LocalDateTime.now(), "/image/천사와악마.jpg", "수채화", park);
		productService.create("산", "이 그림은 장엄한 산의 풍경을 통해 자연의 위대함과 평화를 표현하고 있습니다. 높은 산봉우리가 구름을 뚫고 하늘에 닿아 있으며, 그 위로 펼쳐진 푸른 하늘과 산의 웅장한 실루엣이 돋보입니다. 산의 거친 표면과 부드러운 구름의 조화는 자연의 강인함과 동시에 그 속에서 느껴지는 평온함을 함께 전달합니다. 이 작품은 보는 이에게 자연의 신비롭고 장대한 아름다움을 경험할 수 있는 기회를 제공합니다.", "종이에 물감", "128x85cm", new BigDecimal(13000), LocalDateTime.now(), "/image/산사진.jpg", "수채화", lee);
		productService.create("노란머리여자 ", "이 작품은 노란 머리를 가진 여성의 초상화를 통해 개성과 독특함을 강조합니다. 여성의 머리카락은 밝고 눈에 띄는 노란색으로 칠해져 있으며, 이는 작품의 중심적인 시각적 요소로 작용합니다. 그녀의 표정은 자신감과 매력을 뽐내며, 배경의 단순화된 색조와 대조를 이루어 주목도를 높입니다. 이 그림은 현대적이고 신선한 감각을 전달하며, 개성과 스타일을 표현하는데 중점을 둡니다.", "종이에 물감", "93x128cm", new BigDecimal(14000),  LocalDateTime.now(), "/image/노란머리여자사진.jpg", "수채화", kim);
		productService.create("슬픈사람", "이 작품은 깊은 감정의 표현을 통해 슬픔을 시각적으로 담아낸 초상화입니다. 주인공의 얼굴에는 절망과 상실의 흔적이 명확하게 드러나며, 어두운 색조와 감정적인 구도로 그 분위기를 한층 강조합니다. 눈빛은 슬픔을 고백하는 듯하며, 고요하고 침착한 표정 속에 복잡한 감정이 숨겨져 있습니다. 배경은 단순화되어 있어 인물의 감정에 집중할 수 있도록 돕습니다. 이 그림은 인간의 내면의 고통과 감정을 세심하게 표현하여 보는 이로 하여금 깊은 공감을 일으키게 합니다.", "종이에 물감", "90x128cm", new BigDecimal(15000), LocalDateTime.now(), "/image/슬픈사람.jpg", "수채화", na);
		productService.create("눈", "이 작품은 단순한 형태의 눈을 통해 깊은 감정을 표현한 초상화입니다. 섬세하게 그려진 눈동자는 보는 이로 하여금 감정의 미세한 변화를 읽을 수 있게 해줍니다. 눈의 윤곽과 세밀한 디테일은 인간의 내면 깊숙한 곳까지 들여다볼 수 있는 창을 제공합니다. 강렬한 색조와 명암 대비는 눈의 감정적 강도를 강조하며, 간결한 배경은 감상의 집중을 돕습니다. 이 그림은 시각적으로 감정을 전하며, 보는 이에게 깊은 인상을 남깁니다.", "종이에 색연필과 물감", "128x85cm", new BigDecimal(60000), LocalDateTime.now(), "/image/눈사진.jpg", "수채화", park);
		productService.create("땅하늘우주", "이 작품은 자연과 우주를 아우르는 광대한 경관을 시각적으로 풀어낸 걸작입니다. 하늘과 땅, 우주가 유기적으로 연결된 이 그림은 지구의 고요한 풍경부터 시작하여, 끝없이 펼쳐진 하늘과 별들이 가득한 우주를 배경으로 삼아 마치 모든 것이 하나로 엮여 있는 듯한 느낌을 줍니다. 땅의 구석구석은 현실적이고 섬세하게 묘사되며, 하늘은 푸르고 깊은 색조로 그려져 우주와의 경계를 희미하게 만들어, 우주 속의 무한한 가능성과 신비로움을 상기시킵니다. 이 그림은 자연과 우주의 조화로운 만남을 통해 감상자에게 경이롭고 평화로운 감정을 선사합니다.", "종이에 물감", "21x30cm", new BigDecimal(100000), LocalDateTime.now(), "/image/땅하늘우주.jpg", "수채화", lee);
		productService.create("바다", "이 그림은 무한한 바다의 광활함과 평온함을 담아낸 작품입니다. 해안선을 따라 펼쳐진 푸른 바다는 끝없는 자유와 여유를 상징하며, 파도의 부드러운 곡선과 섬세한 색감이 자연의 아름다움을 강조합니다. 태양의 반사와 물결의 흐름은 생동감을 더하며, 수평선에서 만나는 하늘과 바다는 무한한 상상의 공간을 제공합니다. 이 작품은 바다의 평화롭고 동시에 강렬한 에너지를 시각적으로 표현하여 감상자에게 깊은 힐링을 선사합니다.", "종이에 물감", "128x91cm", new BigDecimal(500000), LocalDateTime.now(), "/image/바다.jpg", "수채화", lee);






		//QNA 테스트 데이터
		answerService.create(questionService.create(   new QuestionForm(  "작가의 주된 작품 스타일은 무엇인가요?", "content", QuestionType.ARTIST), M1),  "작가는 주로 추상 미술과 표현주의 스타일을 사용하며, 감정과 내면의 상태를 형상화하는 데 집중합니다", M1);
		answerService.create(questionService.create(   new QuestionForm(  "미술품 경매를 할수있는 작가의 기준은무엇인가요?", "content", QuestionType.ARTIST), M1),  "저희 사이트에서 작가 기준은 예술활동 증명서를 바탕으로 검토를하여 작가자격심사가 이루어집니다.", M1);
		answerService.create(questionService.create(   new QuestionForm(  "한정판에디션은 무엇인가요?", "content", QuestionType.ARTWORK), M1),  "작가의 한정된 수로 제작하는 작품’을 뜻하며, 작가가 결정합니다. 수량이적을수록 시간이흐를수록 값어치는 점점높아집니다.", M1);
		answerService.create(questionService.create(   new QuestionForm(  "미술품 판매기준이무엇인가요?", "content", QuestionType.ARTWORK), M1),  "미술품 판매기준은 따로 지정해놓지않았지만 작가가 자신의미술품을 올리는것이며 사람마다 기준이달라 미술품판매기준은 작가에게 있습니다.", M1);
		answerService.create(questionService.create(   new QuestionForm(  "구매는 어떻게하나요?", "content", QuestionType.PURCHASE), M1),  "작가가 미술품을판매하면 경매날짜가 정해집니다 지정날짜가되면 경매가시작되고 입찰을통해 경매마감시 제일높은입찰자에게 판매가됩니다.", M1);
		answerService.create(questionService.create(   new QuestionForm(  "입찰 방법을알고싶습니다.", "content", QuestionType.PURCHASE), M1),  "충전을하여 입찰캐시를 얻게됩니다 경매물품에 원하는만큼 입찰을하게되면 소유한 캐시에서 차감이되며 더높은입찰자가생겼을때 입찰한금액을 반환받게됩니다.", M1);
		answerService.create(questionService.create(   new QuestionForm(  "입찰취소방법", "content", QuestionType.PURCHASE), M1),  "입찰하면 다른누군가 더높은금액을 받기전까진 취소할수없습니다. 그점은 주의부탁드리겠습니다.", M1);
		answerService.create(questionService.create(   new QuestionForm(  "사이트에서 제공하는 예술 작품에 대한 상담 서비스는 어떤 것이 있나요?", "content", QuestionType.SERVICE), M1),  "저희 사이트는 전문가 상담 서비스를 제공하여, 작품에 대한 정보와 추천을 받을 수 있습니다. 상담을 통해 작품의 가치와 적합성에 대해 도움을 드립니다.", M1);
		answerService.create(questionService.create(   new QuestionForm(  "미술품 보관 및 관리 서비스는 어떻게 제공받나요?", "content", QuestionType.SERVICE), M1),  "저희는 전체적인미술품 보관 및 관리 서비스를 제공하고있지않지만, 작가혹은 고객상담을통해 보관 및 관리서비스도 진행하고있습니다. 최적의 환경에서 안전하게 보관합니다. 정기적인 관리와 점검이 포함되어 있습니다.", M1);
		answerService.create(questionService.create(   new QuestionForm(  "간단하게질문 답을 빨리받고싶습니다.", "content", QuestionType.SERVICE), M1),  "간단한 질문같은경우는 오른쪽하단 채팅버튼으로 문의주실수있습니다.", M1);
		answerService.create(questionService.create(   new QuestionForm(  "배송시 액자는 어떻게되는거죠", "content", QuestionType.DELIVERY), M1),  "경매,입찰까지 끝나면 배송이되는데 배송관련은 액자까지해서 같이 배송됩니다 구매자께서 액자준비는 안하셔도됩니다.", M1);
		answerService.create(questionService.create(   new QuestionForm(  "배송비는 어떻게 책정되나요?", "content", QuestionType.DELIVERY), M1),  "배송비는 작품의 크기와 무게, 배송 지역에 따라 다르게 책정됩니다. 결제 단계에서 정확한 배송비를 확인하실 수 있습니다.", M1);
		answerService.create(questionService.create(   new QuestionForm(  "배송 시 작품이 손상되지 않도록 어떻게 보호하나요?", "content", QuestionType.DELIVERY), M1),  "작품은 전문 포장 재료를 사용하여 포장되며, 충격 완화재와 보호재를 통해 안전하게 배송됩니다. 포장 과정에서 품질 검사를 실시하여 손상을 최소화합니다.", M1);
		answerService.create(questionService.create(   new QuestionForm(  "작품 반품 절차는 어떻게 되나요?", "content", QuestionType.RETURN), M1),  "작품을 반품하려면, 수령 후 7일 이내에 반품 요청을 하셔야 합니다. 작품이 원 상태로 반송되어야 하며, 반품 비용은 고객님 부담입니다.", M1);
		answerService.create(questionService.create(   new QuestionForm(  "반품 시 작품이 손상된 경우 어떻게 처리하나요?", "content", QuestionType.RETURN), M1),  "반품된 작품이 손상된 경우, 손상 정도에 따라 환불이 불가능할 수 있습니다. 손상된 부분에 대해 검토 후 안내 드리겠습니다.", M1);
		answerService.create(questionService.create(   new QuestionForm(  "배송받았는데 작품이 파손되어있습니다.", "content", QuestionType.RETURN), M1),  "배송완료후 작품이파손된경우 바로문의주시면 환불을도와드립니다.", M1);

	}

}
