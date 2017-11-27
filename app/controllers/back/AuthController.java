package controllers.back;

import java.util.List;
import java.util.stream.Collectors;

import models.auth.Auth;
import vos.AuthVO;
import vos.PageData;
import vos.Result;

public class AuthController extends BackController {

	public static void list(AuthVO vo) {
		List<Auth> auths = Auth.fetch(vo);
		List<AuthVO> authVOs = auths.stream().map(a -> new AuthVO(a)).collect(Collectors.toList());
		renderJSON(Result.succeed(new PageData(authVOs)));
	}

	public static void add(AuthVO vo) {
		Auth.add(vo);
		renderJSON(Result.succeed());
	}

	public static void edit(AuthVO vo) {
		Auth auth = Auth.findByID(vo.id);
		auth.edit(vo);
		renderJSON(Result.succeed());
	}

	public static void del(long id) {
		Auth auth = Auth.findByID(id);
		auth.del();
		renderJSON(Result.succeed());
	}

}
