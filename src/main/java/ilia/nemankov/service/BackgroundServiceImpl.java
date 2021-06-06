package ilia.nemankov.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import ilia.nemankov.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BackgroundServiceImpl implements BackgroundService {

    private final UserService userService;

    @Value("${bonuses.daily.value}")
    private Integer dailyBonus;

    @Value("${bonuses.daily.maxamount}")
    private Integer maxAmount;

    @Value("${is.main.instance}")
    private Boolean isMainInstance;

    public BackgroundServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Scheduled(cron="0 0 0 * * *")
    public void addDailyBonuses() {
        if (!isMainInstance) {
            return;
        }

        List<UserDTO> users = userService.getAllUsers();

        for (UserDTO user : users) {
            try {
                int newBonusesValue = user.getBonuses() + dailyBonus;

                if (newBonusesValue <= maxAmount) {
                    user.setBonuses(newBonusesValue);
                    userService.updateUserBonuses(user);
                } else if (user.getBonuses() < maxAmount) {
                    user.setBonuses(maxAmount);
                    userService.updateUserBonuses(user);
                }
            } catch (Exception e) {
                System.out.println("Failed to add daily bonuses to user with login: " + user.getLogin());
            }
        }
    }

}
