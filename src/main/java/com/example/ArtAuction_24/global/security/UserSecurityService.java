package com.example.ArtAuction_24.global.security;

import com.example.ArtAuction_24.domain.artist.entity.Artist;
import com.example.ArtAuction_24.domain.artist.repository.ArtistRepository;
import com.example.ArtAuction_24.domain.member.entity.Member;
import com.example.ArtAuction_24.domain.member.entity.MemberRole;
import com.example.ArtAuction_24.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> _member = this.memberRepository.findByUsername(username);

        if (_member.isEmpty()) {
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
        }

        Member member = _member.get();
        List<GrantedAuthority> authorities = new ArrayList<>();

        if ( "admin".equals(username) ) {
            authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(member.getRole().getValue()));
        }


        return new User(member.getUsername(), member.getPassword(), authorities);
    }
}