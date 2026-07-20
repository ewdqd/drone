package com.houduan.drone.Service.rpc;

import com.houduan.drone.Config.HttpResultResponse;
import com.houduan.drone.Dto.QueryDto;
import com.houduan.drone.Dto.UsersDTO;

/**
 * Dubbo RPC interface for user service.
 * Gateway calls this via Dubbo to perform user operations.
 */
public interface UserRpcService {

    /**
     * User login — validate credentials and return user info.
     */
    HttpResultResponse login(String username, String password);

    /**
     * Get user by ID.
     */
    HttpResultResponse getUserById(Integer id);

    /**
     * Register a new user.
     */
    HttpResultResponse register(UsersDTO usersDTO);

    /**
     * Modify an existing user.
     */
    HttpResultResponse modify(UsersDTO usersDTO);

    /**
     * Delete user by ID.
     */
    HttpResultResponse delete(Integer id);

    /**
     * Paginated user list.
     */
    HttpResultResponse pageUsers(QueryDto queryDto);
}
