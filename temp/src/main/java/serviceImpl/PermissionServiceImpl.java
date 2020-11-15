package .serviceImpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import .service.IPermissionService;
import .entity.*;
import .common.*;
import .request.*;
import .mapper.*;
import .dto.*;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public Permission add(Permission permission) {
        save(permission);
        return getById(permission.getId());
    }

    @Override
    public Permission update(Permission permission) {
        updateById(permission);
        return getById(permission.getId());
    }

    @Override
    public Permission detail(int id) {
        return getById(id);
    }

    @Override
    public void delete(int id) {
        getBaseMapper().deleteById(id);
    }

    @Override
    public ListResponse<Permission> list(ListPermissionRequest request) {
        IPage<Permission> page = page(new Page<>(request.getPageNumber(), request.getPageSize()));
        return new ListResponse<>(page.getRecords(), page.getTotal());
    }

}
