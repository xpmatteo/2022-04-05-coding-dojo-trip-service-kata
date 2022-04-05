package org.craftedsw.tripservicekata.trip;

import org.craftedsw.tripservicekata.exception.UserNotLoggedInException;
import org.craftedsw.tripservicekata.user.User;
import org.craftedsw.tripservicekata.user.UserSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class TripServiceTest {
    private List<Trip> friendTrips;
    private UserSession userSessionMock;
    private TripService tripService;

    @BeforeEach
    void setUp() {
        userSessionMock = mock(UserSession.class);
        tripService = new TripService() {
            @Override
            protected UserSession getUserSession() {
                return userSessionMock;
            }

            @Override
            protected List<Trip> getFriendTrips(User user) {
                return friendTrips;
            }
        };
    }

    @Test
    void userNotLoggedIn() {
        Assertions.assertThrows(UserNotLoggedInException.class, () -> tripService.getTripsByUser(new User()));
    }

    @Test
    void userLoggedInNoFriends() {
        doReturn(new User()).when(userSessionMock).getLoggedUser();
        assertThat(tripService.getTripsByUser(new User())).isEqualTo(Collections.emptyList());
    }

    @Test
    void userLoggedInWithFriendsNoTrips() {
        User loggedUser = new User();
        doReturn(loggedUser).when(userSessionMock).getLoggedUser();

        User user = new User();
        user.addFriend(loggedUser);

        friendTrips = Collections.emptyList();

        assertThat(tripService.getTripsByUser(user)).isEqualTo(Collections.emptyList());
    }

    @Test
    void userLoggedInWithFriendsWithTrips() {
        User loggedUser = new User();
        doReturn(loggedUser).when(userSessionMock).getLoggedUser();

        User user = new User();
        user.addFriend(loggedUser);

        friendTrips = Collections.singletonList(new Trip());

        List<Trip> actualTrips = tripService.getTripsByUser(user);
        assertThat(actualTrips).isEqualTo(friendTrips);
    }
}
