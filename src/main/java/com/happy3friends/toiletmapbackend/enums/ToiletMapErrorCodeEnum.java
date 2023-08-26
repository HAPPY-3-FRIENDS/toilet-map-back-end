package com.happy3friends.toiletmapbackend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ToiletMapErrorCodeEnum {

    // 400 - BAD REQUEST
    EMPTY_PASSWORD(HttpStatus.BAD_REQUEST, 400000, "Mật khẩu rỗng"),
    EMPTY_COMPANY_ID(HttpStatus.BAD_REQUEST, 400001, "Company ID rỗng"),
    INVALID_ROLE(HttpStatus.BAD_REQUEST, 400002, "Role không hợp lệ"),
    CREATE_ACCOUNT_ADMIN_ERROR(HttpStatus.BAD_REQUEST, 400003, "Không thể tạo tài khoản cho Admin"),
    ADMIN_CREATE_MANAGER_ONLY(HttpStatus.BAD_REQUEST, 400004, "Admin chỉ được tạo tài khoản cho Manager"),
    MANAGER_CREATE_STAFF_ONLY(HttpStatus.BAD_REQUEST, 400005, "Manager chỉ dược tạo tài khoản cho Staff"),
    EXISTED_USERNAME(HttpStatus.BAD_REQUEST, 400006, "Tên tài khoản đã tồn tại"),
    EXISTED_PHONE(HttpStatus.BAD_REQUEST, 400007, "Số điện thoại đã tồn tại"),
    INVALID_DEFAULT_PAYMENT_METHOD(HttpStatus.BAD_REQUEST, 400008, "Phương thức thanh toán mặc định của tài khoản không hợp lệ"),
    ACCOUNT_BALANCE_NOT_ENOUGH(HttpStatus.BAD_REQUEST, 400009, "Số dư tài khoản không đủ"),
    ACCOUNT_TURN_NOT_ENOUGH(HttpStatus.BAD_REQUEST, 400010, "Số lượt tài khoản không đủ"),
    EXPIRED_QR_CODE(HttpStatus.BAD_REQUEST, 400011, "QR Code hết hạn"),
    INVALID_SERVICE(HttpStatus.BAD_REQUEST, 400012, "Dịch vụ không hợp lệ"),
    INVALID_PAYMENT_METHOD(HttpStatus.BAD_REQUEST, 400013, "Phương thức thanh toán không hợp lệ"),
    FROM_DATE_AFTER_TO_DATE(HttpStatus.BAD_REQUEST, 400014, "Ngày bắt đầu đến sau ngày kết thúc"),
    INVALID_STATUS(HttpStatus.BAD_REQUEST, 400015, "Trạng thái không hợp lệ"),
    INVALID_FACILITY(HttpStatus.BAD_REQUEST, 400016, "Trang thiết bị không hợp lệ"),
    INVALID_ROOM_QUANTITY(HttpStatus.BAD_REQUEST, 400017, "Số lượng của phòng vệ sinh không hợp lệ"),
    INVALID_VALIDATION(HttpStatus.BAD_REQUEST, 400018, "Tham số không hợp lệ"),
    CREATE_COMPANY_ERROR(HttpStatus.BAD_REQUEST, 400019, "Tạo công ty thất bại"),
    EXISTED_RATING(HttpStatus.BAD_REQUEST, 400020, "Đánh giá đã tồn tại"),
    EXPIRED_RATING(HttpStatus.BAD_REQUEST, 400021, "Đánh giá hết hạn"),
    EXISTED_FACILITY(HttpStatus.BAD_REQUEST, 400022, "Tên thiết bị đã tồn tại"),
    EXISTED_FACILITY_IN_USE(HttpStatus.BAD_REQUEST, 400023, "Thiết bị đang được sử dụng"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, 400024, "Mật khẩu không hợp lệ"),
    INVALID_UPDATED_FACILITY(HttpStatus.BAD_REQUEST, 400025, "Trang thiết bị cần cập nhật không hợp lệ"),
    INVALID_COMBO_TOTALTURN_QUANTITY(HttpStatus.BAD_REQUEST, 400026, "Số lượng của tổng lượt của gói lượt không hợp lệ"),
    INVALID_PRICE(HttpStatus.BAD_REQUEST, 400027, "Giá không hợp lệ"),
    NOT_ENOUGH_USER(HttpStatus.BAD_REQUEST, 400028, "Hệ thống không đủ user"),
    TOILET_FULL_RESTROOM(HttpStatus.BAD_REQUEST, 400029, "Nhà vệ sinh không còn phòng đại tiện"),
    TOILET_FULL_BATHROOM(HttpStatus.BAD_REQUEST, 400030, "Nhà vệ sinh không còn phòng tắm"),
    RESTROOM_NOT_ENOUGH(HttpStatus.BAD_REQUEST, 400031, "Phòng đại tiện không đủ"),
    BATHROOM_NOT_ENOUGH(HttpStatus.BAD_REQUEST, 400032, "Phòng tắm không đủ"),

    // 401 - UNAUTHENTICATED
    UNAUTHENTICATED(HttpStatus.UNAUTHORIZED, 401000, "Chưa xác thực người dùng"),

    // 403 - UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.FORBIDDEN, 403000, "Không có quyền truy cập"),

    // 404 - NOT FOUND
    NOT_FOUND_COMPANY(HttpStatus.NOT_FOUND, 404000, "Không tìm thấy Công ty"),
    NOT_FOUND_TOILET(HttpStatus.NOT_FOUND, 404001, "Không tìm thấy Nhà vệ sinh"),
    NOT_FOUND_ACCOUNT(HttpStatus.NOT_FOUND, 404002, "Không tìm thấy Tài khoản"),
    NOT_FOUND_LIST_TOILET_SERVICES(HttpStatus.NOT_FOUND, 404003, "Không tìm thấy danh sách các dịch vụ của Nhà vệ sinh"),
    NOT_FOUND_COMBO(HttpStatus.NOT_FOUND, 404004, "Không tìm thấy Gói lượt"),
    NOT_FOUND_USER_INFO(HttpStatus.NOT_FOUND, 404005, "Không tìm thấy thông tin người dùng"),
    NOT_FOUND_TOILET_NEARBY(HttpStatus.NOT_FOUND, 404006, "Không tìm thấy toilet gần đây"),
    NOT_FOUND_COMMON_COMMENT(HttpStatus.NOT_FOUND, 404007, "Không tìm thấy bình luận thông dụng"),
    NOT_FOUND_SERVICE(HttpStatus.NOT_FOUND, 404008, "Không tìm thấy dịch vụ"),
    NOT_FOUND_SENSITIVE_WORD(HttpStatus.NOT_FOUND, 404009, "Không tìm thấy từ nhạy cảm"),
    NOT_FOUND_RATING(HttpStatus.NOT_FOUND, 404010, "Không tìm thấy từ đánh giá"),
    NOT_FOUND_ANNOUNCEMENT(HttpStatus.NOT_FOUND, 404011, "Không tìm thấy thông báo"),
    NOT_FOUND_SUGGESTION(HttpStatus.NOT_FOUND, 404012, "Không tìm thấy gợi ý"),
    NOT_FOUND_REPORT(HttpStatus.NOT_FOUND, 404013, "Không tìm thấy báo cáo"),
    NOT_FOUND_FACILITY(HttpStatus.NOT_FOUND, 404014, "Không tìm thấy trang thiết bị"),
    NOT_FOUND_CONFIGURATION(HttpStatus.NOT_FOUND, 404015, "Không tìm thấy thiết lập"),

    // VNPAY
    VNPAY_CARD_NOT_REGISTERED_INTERNETBANKING(HttpStatus.INTERNAL_SERVER_ERROR, 500001, "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng."),
    VNPAY_VERIFY_CARD_NOT_CORRECT(HttpStatus.INTERNAL_SERVER_ERROR, 500002, "Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần"),
    VNPAY_PAYMENT_EXPIRED(HttpStatus.INTERNAL_SERVER_ERROR, 500003, "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch."),
    VNPAY_LOCKED_CARD(HttpStatus.INTERNAL_SERVER_ERROR, 500004, "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa."),
    VNPAY_WRONG_OTP(HttpStatus.INTERNAL_SERVER_ERROR, 500005, "Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch."),
    VNPAY_CUSTOMER_CANCEL_TRANSACTION(HttpStatus.INTERNAL_SERVER_ERROR, 500006, "Giao dịch không thành công do: Khách hàng hủy giao dịch"),
    VNPAY_NOT_ENOUGH_BALANCE(HttpStatus.INTERNAL_SERVER_ERROR, 500007, "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch."),
    VNPAY_EXCEEDED_DAILY_TRANSACTION_LIMIT(HttpStatus.INTERNAL_SERVER_ERROR, 500008, "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày."),
    VNPAY_BANK_UNDER_MAINTENANCE(HttpStatus.INTERNAL_SERVER_ERROR, 500009, "Ngân hàng thanh toán đang bảo trì."),
    VNPAY_WRONG_PASSWORD(HttpStatus.INTERNAL_SERVER_ERROR, 500010, "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch"),
    VNPAY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500011, "Các lỗi khác (lỗi còn lại, không có trong danh sách mã lỗi đã liệt kê)"),
    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
