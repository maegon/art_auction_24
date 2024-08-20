package com.example.ArtAuction_24.domain.notification.service;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.auction.repository.AuctionRepository;
import com.example.ArtAuction_24.domain.bid.entity.Bid;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.repository.MemberRepository;
import com.example.ArtAuction_24.domain.notification.entity.Notification;
import com.example.ArtAuction_24.domain.notification.repository.NotificationRepository;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.global.email.EmailService;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender javaMailSender;

    private final EmailService emailService;
    private final NotificationRepository notificationRepository;
    private final AuctionRepository auctionRepository;
    private final MemberRepository memberRepository;
    private final TaskScheduler taskScheduler;
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    // 경매 종료시 알림 발송
    @Transactional
    public void notifyAuctionResults(Auction auction) {
        Set<Member> allBidders = new HashSet<>();
        Set<Member> winningBidders = new HashSet<>();
        Set<Artist> artists = new HashSet<>();

        for (Product product : auction.getProducts()) {

            Set<Member> biddersForProduct = getBiddersForProduct(product);
            allBidders.addAll(biddersForProduct);

            // 낙찰자가 있는 경우
            if (product.getWinningBidder() != null) {
                winningBidders.add(product.getWinningBidder());
                artists.add(product.getArtist());

                // 낙찰자에게 알림 전송
                sendNotification(
                        product.getWinningBidder(),
                        null,
                        product,
                        "🎉 경매 낙찰 알림 🎉",
                        String.format(
                                "<html><body>" +
                                        "<p>안녕하세요, <strong>%s님</strong>.</p>" +
                                        "<p>축하합니다! 🎉</p>" +
                                        "<p>당신이 참여한 경매에서 다음과 같은 멋진 작품을 낙찰 받았습니다!!</p>" +
                                        "<h2>작품 제목: <strong>'%s'</strong></h2>" +
                                        "<p>작가: <strong>%s</strong></p>" +
                                        "<p>경매가 종료되었습니다. 구매 관련 정보는 웹사이트에서 확인하세요.</p>" +
                                        "<p>추가 질문이 있거나 도움이 필요하시면 언제든지 고객 지원 팀에 문의해 주세요.</p>" +
                                        "<p>감사합니다!</p>" +
                                        "<p>귀하의 경매 플랫폼 ARTAUCTION</p>" +
                                        "</body></html>",
                                product.getWinningBidder().getUsername(),
                                product.getTitle(),
                                product.getArtist() != null ? product.getArtist().getKorName() : "알 수 없음"
                        )
                );


                // 작가에게 알림 전송
                sendNotification(
                        null,
                        product.getArtist(),
                        product,
                        "🎨 경매 종료 및 낙찰 알림 🎨",
                        String.format(
                                "<html><body>" +
                                        "<p>안녕하세요, <strong>%s님</strong>.</p>" +
                                        "<p>축하드립니다! 🎉</p>" +
                                        "<p>당신의 작품 '<strong>%s</strong>'이(가) 경매에서 성공적으로 낙찰되었습니다.</p>" +
                                        "<p>아래는 작품의 세부 사항입니다</p>" +
                                        "<h2>작품 제목: <strong>'%s'</strong></h2>" +
                                        "<p>작품에 대한 자세한 사항은 웹사이트에서 확인하실 수 있습니다. 낙찰과 관련된 추가적인 정보는 경매 플랫폼의 관리자에게 문의해 주세요.</p>" +
                                        "<p>이번 경매에 참여해 주셔서 감사드리며, 앞으로도 많은 사랑과 지원 부탁드립니다.</p>" +
                                        "<p>감사합니다!</p>" +
                                        "<p>귀하의 경매 플랫폼 ARTAUCTION</p>" +
                                        "</body></html>",
                                product.getArtist().getKorName(),
                                product.getTitle(),
                                product.getTitle()
                        )
                );


            } else {
                artists.add(product.getArtist());
            }
        }

        // 낙찰되지 않은 입찰자들에게 알림 전송
        // 낙찰되지 않은 입찰자들에게 알림 전송
        allBidders.stream()
                .filter(bidder -> !winningBidders.contains(bidder))
                .forEach(bidder -> sendNotification(
                        bidder,
                        null,
                        null,
                        "경매 결과 안내",
                        String.format(
                                "<html><body>" +
                                        "<p>안녕하세요, <strong>%s님</strong>.</p>" +
                                        "<p>안타깝게도, 이번 경매에서는 낙찰되지 않았습니다.</p>" +
                                        "<p>아래는 경매의 상세 내용입니다</p>" +
                                        "<p>경매 제목: <strong>'%s'</strong></p>" +
                                        "<p>당신의 참여와 성원에 감사드립니다. 경매에 참여해 주신 것에 대해 진심으로 감사드리며, 다음 기회에 더 좋은 결과가 있기를 기대합니다.</p>" +
                                        "<p>다음 경매에 대한 정보는 웹사이트에서 확인하실 수 있습니다. 계속해서 많은 관심과 참여 부탁드립니다.</p>" +
                                        "<p>감사합니다!</p>" +
                                        "<p>귀하의 경매 플랫폼 ARTAUCTION</p>" +
                                        "</body></html>",
                                bidder.getUsername(),
                                auction.getName()
                        )
                ));

    }



    // 입찰자의 리스트를 가져오는 메소드
    private Set<Member> getBiddersForProduct(Product product) {
        Set<Member> bidders = new HashSet<>();
        List<Bid> bids = product.getBids();

        if (bids != null) {
            for (Bid bid : bids) {
                bidders.add(bid.getMember());
            }
        }
        return bidders;
    }

    // 이메일 발송 및 알림 저장
    @Async
    private void sendNotification(Member member, Artist artist, Product product, String subject, String message) {

        boolean isSent = true;

        String recipientEmail = null;
        if (member != null) {
            recipientEmail = member.getEmail();
        }

        if (recipientEmail != null) {
            try {
                // HTML 메일 생성
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                mimeMessageHelper.setTo(recipientEmail);
                mimeMessageHelper.setSubject(subject);
                mimeMessageHelper.setFrom("99gorhs@gmail.com");
                mimeMessageHelper.setText(message, true); // HTML로 설정

                // 메일 전송
                javaMailSender.send(mimeMessage);
            } catch (Exception e) {
                isSent = false;
                logger.error("Failed to send notification to {}: {}", recipientEmail, e.getMessage());
            }

            // 알림을 DB에 저장 (이메일 발송 여부와 관계없이)
            Notification notification = Notification.builder()
                    .member(member)
                    .artist(artist)
                    .product(product)
                    .subject(subject)
                    .message(message)
                    .isSent(isSent)
                    .createDate(LocalDateTime.now())
                    .build();
            notificationRepository.save(notification);


            if (isSent) {
                logger.info("Notification sent and saved for recipient: {}", recipientEmail);
            } else {
                logger.warn("Notification failed and saved for recipient: {}", recipientEmail);
            }
        }
    }

    @Transactional
    public void scheduleAuctionNotification(Long auctionId, String username) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid auction ID"));
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member username"));

        LocalDateTime notificationTime = auction.getStartDate().minusHours(1);

        // 알림 메시지에 경매 제품 목록 포함
        StringBuilder productListBuilder = new StringBuilder("경매 제품 목록:\n");
        Set<Product> products = auction.getProducts();
        for (Product product : products) {
            productListBuilder.append("- ").append(product.getTitle()).append("\n");
        }

        String message = String.format(
                "<html>" +
                        "<body style='font-family: Arial, sans-serif;'>" +
                        "<div style='background-color: #f4f4f4; padding: 20px; border-radius: 8px;'>" +
                        "<h2 style='color: #333;'>경매 시작 알림</h2>" +
                        "<p style='font-size: 16px;'>안녕하세요, <strong>%s님</strong>,</p>" +
                        "<p style='font-size: 16px;'>경매가 곧 시작됩니다! 아래는 알림설정된 제품입니다</p>" +
                        "<ul style='list-style-type: square; margin-left: 20px;'>%s</ul>" +
                        "<p style='font-size: 16px;'>경매 시작 시간: <strong>%s</strong></p>" +
                        "<p style='font-size: 16px;'>경매에 대한 더 많은 정보는 웹사이트에서 확인하세요.</p>" +
                        "<p style='font-size: 16px;'>감사합니다!</p>" +
                        "<p style='font-size: 16px;'>귀하의 경매 플랫폼 ARTAUCTION</p>" +
                        "</div>" +
                        "</body>" +
                        "</html>",
                member.getUsername(),
                productListBuilder.toString(),
                auction.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        );


        Notification notification = Notification.builder()
                .message(message)
                .recipient(member.getEmail())
                .subject("경매 시작 1시간 전 알림")
                .isSent(false)
                .sentAt(notificationTime)
                .type(Notification.NotificationType.EMAIL)
                .member(member)
                .build();

        notificationRepository.save(notification);

        // 알림 예약 (1시간 전)
        scheduleEmailNotification(notification);
    }

    private void scheduleEmailNotification(Notification notification) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime notificationTime = notification.getSentAt();

        // 지연 시간 계산
        Duration delay = Duration.between(now, notificationTime);

        taskScheduler.schedule(() -> sendEmailNotification(notification),
                notificationTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private void sendEmailNotification(Notification notification) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            mimeMessageHelper.setTo(notification.getRecipient());
            mimeMessageHelper.setSubject(notification.getSubject());
            mimeMessageHelper.setFrom("99gorhs@gmail.com");
            mimeMessageHelper.setText(notification.getMessage(), true); // HTML로 설정

            javaMailSender.send(mimeMessage);

            notification.setSent(true);
            notification.setSentAt(LocalDateTime.now());
            notificationRepository.save(notification);
        } catch (Exception e) {
            logger.error("Failed to send notification: {}", e.getMessage());
        }
    }
}
