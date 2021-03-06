package egovframework.com.cmm.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import egovframework.com.cmm.LoginVO;

import egovframework.rte.fdl.string.EgovObjectUtil;

import org.apache.commons.logging.impl.SimpleLog;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * EgovUserDetails Helper 클래스
 * 
 * @author sjyoon
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    -------------    ----------------------
 *   2009.03.10  sjyoon    최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 * </pre>
 */

public class EgovUserDetailsHelper {
	
		private static Logger logger = Logger.getLogger(SimpleLog.class);
		/**
		 * 인증된 사용자객체를 VO형식으로 가져온다.
		 * @return Object - 사용자 ValueObject
		 */
		public static Object getAuthenticatedUser() {
			return (LoginVO)RequestContextHolder.getRequestAttributes().getAttribute("LoginVO", RequestAttributes.SCOPE_SESSION)==null ? 
					new LoginVO() : (LoginVO) RequestContextHolder.getRequestAttributes().getAttribute("LoginVO", RequestAttributes.SCOPE_SESSION);

		}

		/**
		 * 인증된 사용자의 권한 정보를 가져온다.
		 * 예) [ROLE_ADMIN, ROLE_USER, ROLE_A, ROLE_B, ROLE_RESTRICTED, IS_AUTHENTICATED_FULLY, IS_AUTHENTICATED_REMEMBERED, IS_AUTHENTICATED_ANONYMOUSLY]
		 * @return List - 사용자 권한정보 목록
		 */
		public static List<String> getAuthorities() {
			List<String> listAuth = new ArrayList<String>();
			
			if (EgovObjectUtil.isNull((LoginVO) RequestContextHolder.getRequestAttributes().getAttribute("LoginVO", RequestAttributes.SCOPE_SESSION))) {
				logger.debug("## authentication object is null!!");//수업시 log사용법 확인
				// 아이디/암호 인증을 받지 않은 사람은 List<String> listAuth 에 null리턴
				return null;
			}
			
			//*스프링 시큐리티 연동 추가 시작
			/* 권한 값 자동등록이 되지 않아서 수동 등록 코드 Start */
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String listAuthTemp1 = authentication.getAuthorities().toString();
			System.out.println("디버그 권한그룹 테이블의 쿼리 결과" + listAuthTemp1);
			listAuthTemp1 = listAuthTemp1.replace("[", "");
			listAuthTemp1 = listAuthTemp1.replace("]", "");
			listAuthTemp1 = listAuthTemp1.replace(" ", "");
			String[] listAuthTemp2 = listAuthTemp1.split(",");
			//for(int i=0;i<listAuthTemp2.length;i++) {
			//	listAuth.add(listAuthTemp2[i]);
			//}
			listAuth = Arrays.asList(listAuthTemp2);
			System.out.println("EgovUserDetailsHelper 실행");
			/* 권한 값 자동등록이 되지 않아서 수동 등록 코드 End */
			//*스프링 시큐리티 연동 추가 끝
			
			return listAuth;
		}
		
		/**
		 * 인증된 사용자 여부를 체크한다.
		 * @return Boolean - 인증된 사용자 여부(TRUE / FALSE)	
		 */
		public static Boolean isAuthenticated() {
			
			if (EgovObjectUtil.isNull((LoginVO) RequestContextHolder.getRequestAttributes().getAttribute("LoginVO", RequestAttributes.SCOPE_SESSION))) {
				// log.debug("## authentication object is null!!");
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}
}