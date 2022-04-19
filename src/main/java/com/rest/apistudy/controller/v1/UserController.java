package com.rest.apistudy.controller.v1;

import com.rest.apistudy.entity.User;
import com.rest.apistudy.exception.CUserNotFoundException;
import com.rest.apistudy.model.response.CommonResult;
import com.rest.apistudy.model.response.SingleResult;
import com.rest.apistudy.repo.UserJpaRepo;
import com.rest.apistudy.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class UserController {
    private final UserJpaRepo userJpaRepo;
    private final ResponseService responseService;

    @GetMapping("/user")
    public List<User> findAllUser() {
        return userJpaRepo.findAll();
    }

    @GetMapping(value = "/user/{userId}")
    public SingleResult<User> findUserById(@PathVariable int userId) {

        // 결과데이터가 단일건인경우 getSingleResult를 이용해서 결과를 출력한다.
        return responseService.getSingleResult(userJpaRepo.findById((long) userId).orElseThrow(()->new CUserNotFoundException()));
    }

    @PostMapping("/user")
    public SingleResult<User> save(@RequestParam String uid, @RequestParam String name) {

        User user = User.builder()
                .uid(uid)
                .name(name)
                .build();
        return responseService.getSingleResult(userJpaRepo.save(user));
    }

    @PutMapping("/user")
    public SingleResult<User> modify(@RequestParam long msrl, @RequestParam String uid, @RequestParam String name) {
        User user = User.builder()
                .msrl(msrl)
                .uid(uid)
                .name(name)
                .build();
        return responseService.getSingleResult(userJpaRepo.save(user));
    }

    @DeleteMapping("/user/{msrl}")
    public CommonResult delete(@PathVariable long msrl) {
        userJpaRepo.deleteById(msrl);
        return responseService.getSuccessResult();

    }
}
