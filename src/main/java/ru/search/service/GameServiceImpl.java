package ru.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.exeptions.NotFoundException;
import ru.search.SearchMessageType;
import ru.search.SearchMessage;
import ru.session.SessionRepository;
import ru.session.model.Session;
import ru.user.UserRepository;
import ru.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameServiceImpl implements GameService{
    private final List<User> searchUsers = new ArrayList<>();
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    @Override
    public SearchMessage addSearch(Long userId) {

        //Получаем пользователя
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id=" + userId + " was not found"));

        //Проверяем, есть ли в поиске уже кто-то (Тут по идее как-то проверять, что чел в поиске не отвалился)
        if(!searchUsers.isEmpty()) {

            //Забираем пользователя, который был добавлен раньше всех
            User otherUser = searchUsers.get(0);
            searchUsers.remove(0);

            //Формируем сессию
            Session session = new Session(user, otherUser);
            sessionRepository.save(session);

            return new SearchMessage(
                    SearchMessageType.GAME_FOUND,
                    session.getId()
            );
        }

        //Добавляем в список
        searchUsers.add(user);

        //Отправляем сообщение о добавлении в очередь
        SearchMessage searchMessage = new SearchMessage();
        searchMessage.setMessageType(SearchMessageType.SEARCH);
        return searchMessage;
    }
}
