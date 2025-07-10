package cineconecta.cineconecta.service.User;

import cineconecta.cineconecta.model.Users.Cinephile;
import cineconecta.cineconecta.model.Users.Profile;
import cineconecta.cineconecta.repository.Users.CinephileRepository;
import cineconecta.cineconecta.repository.Users.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServices {

    private final CinephileRepository cinephileRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    public String registerUser(Cinephile cinephile_data) {
        Cinephile cinephile = new Cinephile();
        Profile profile = new Profile();
        String password_hash = passwordEncoder.encode(cinephile_data.getPassword());
        profileRepository.save(profile);


        cinephile.setName(cinephile_data.getName());
        cinephile.setEmail(cinephile_data.getEmail());
        cinephile.setPassword(password_hash);
        cinephile.setPhoneNumber(cinephile_data.getPhoneNumber());
        cinephile.setProfile(profile);

        cinephileRepository.save(cinephile);

        return "User registered successfully";
    }
}
