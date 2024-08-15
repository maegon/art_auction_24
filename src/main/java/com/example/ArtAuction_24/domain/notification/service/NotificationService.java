package com.example.ArtAuction_24.domain.notification.service;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.auction.entity.Auction;
import com.example.ArtAuction_24.domain.bid.entity.Bid;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.notification.entity.Notification;
import com.example.ArtAuction_24.domain.notification.repository.NotificationRepository;
import com.example.ArtAuction_24.domain.product.entity.Product;
import com.example.ArtAuction_24.global.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final EmailService emailService;
    private final NotificationRepository notificationRepository;
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public void notifyAuctionResults(Auction auction) {
        Set<Member> allBidders = new HashSet<>();
        Set<Member> winningBidders = new HashSet<>();
        Set<Artist> artists = new HashSet<>();

        // 모든 입찰자와 낙찰자 및 작가 추출
        for (Product product : auction.getProducts()) {
            Set<Member> biddersForProduct = getBiddersForProduct(product);
            allBidders.addAll(biddersForProduct);

            if (product.getWinningBidder() != null) {
                winningBidders.add(product.getWinningBidder());
                artists.add(product.getArtist());

                // 낙찰자에게 알림 전송
                sendNotification(
                        product.getWinningBidder().getEmail(),
                        "경매 낙찰 알림",
                        String.format(
                                "안녕하세요, %s님. 축하합니다! '%s' 작품을 낙찰 받았습니다. 구매 관련 정보는 웹사이트에서 확인하세요.",
                                product.getWinningBidder().getUsername(), product.getTitle()
                        ),
                        Notification.NotificationType.EMAIL
                );

                // 작가에게 알림 전송
                sendNotification(
                        product.getArtist().getMail(),
                        "경매 종료 알림",
                        String.format(
                                "안녕하세요, %s님. '%s' 작품이 경매에서 낙찰되었습니다. 자세한 사항은 웹사이트에서 확인하세요.",
                                product.getArtist().getKorName(), product.getTitle()
                        ),
                        Notification.NotificationType.EMAIL
                );
            } else {
                artists.add(product.getArtist());
            }
        }

        // 낙찰되지 않은 입찰자들에게 알림 전송
        allBidders.stream()
                .filter(bidder -> !winningBidders.contains(bidder))
                .forEach(bidder -> sendNotification(
                        bidder.getEmail(),
                        "경매 결과 안내",
                        String.format(
                                "안녕하세요, %s님. 안타깝게도 이번 경매에서 낙찰되지 않았습니다. 다음 기회에 더 좋은 결과를 기대합니다.",
                                bidder.getUsername()
                        ),
                        Notification.NotificationType.EMAIL
                ));
    }

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

    private void sendNotification(String recipient, String subject, String message, Notification.NotificationType type) {
        boolean isSent = true;

        try {
            emailService.send(recipient, subject, message);
        } catch (Exception e) {
            isSent = false;
            logger.error("Failed to send notification to {}: {}", recipient, e.getMessage());
        }

        Notification notification = Notification.builder()
                .recipient(recipient)
                .message(message)
                .isSent(isSent)
                .sentAt(LocalDateTime.now())
                .type(type)
                .build();

        notificationRepository.save(notification);

        if (isSent) {
            logger.info("Notification sent and saved for recipient: {}", recipient);
        } else {
            logger.warn("Notification failed and saved for recipient: {}", recipient);
        }
    }
}
