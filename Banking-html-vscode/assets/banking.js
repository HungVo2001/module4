
let bankingSelected = {};
const url = 'http://localhost:3300/customers';
let page = 0;
let size = 10;
let totalPage = 0;

const validateName = (event) => {
    const input = event.target;
    const errorContainer = input.nextElementSibling; // lấy phần tử kế tiếp (container lỗi)
    const value = input.value;
    // thực hiện kiểm tra giá trị ở đây
    if (value.trim() === '') {
        errorContainer.textContent = 'This field is required!';
    }
}
const validatePhone = (event) => {
    const input = event.target;
    const errorContainer = input.nextElementSibling;
    const value = input.value;

    if (value.trim() === '') {
        errorContainer.textContent = 'This field is required!';
    }
}

const validateEmail = (event) => {
    const input = event.target;
    const errorContainer = input.nextElementSibling;
    const value = input.value;

    if (value.trim() === '') {
        errorContainer.textContent = 'This field is required!';
    }
}

const validateAddress = (event) => {
    const input = event.target;
    const errorContainer = input.nextElementSibling; 
    const value = input.value;
    
    if (value.trim() === '') {
        errorContainer.textContent = 'This field is required!';
    }
}

const clearError = (event) => {
    const input = event.target;
    const errorContainer = input.nextElementSibling;
    errorContainer.textContent = ''; // xóa thông báo lỗi khi thay đổi giá trị
}

const renderCustomer = (item) => {
    return `
                <tr id="tr_${item.id}">
                    <td>${item.id}</td>
                    <td>${item.fullName}</td>
                    <td>${item.email}</td>
                    <td>${item.phone}</td>
                    <td id="balance-${item.id}">${item.balance}</td>
                    <td>
                        <button class="btn btn-outline-primary edit" data-id="${item.id}" data-bs-toggle="modal" data-bs-target="#modalUpdate">
                            <i class="fas fa-user-edit"></i>
                        </button>
                    </td>
                    <td>
                        <button class="btn btn-outline-success" onclick="showPlus(${item.id})" data-bs-toggle="modal" data-bs-target="#modalPlus">
                            <i class="fas fa-plus"></i>
                        </button>
                    </td>
                    <td>
                        <button class="btn btn-outline-warning" onclick="showMinus(${item.id})" data-bs-toggle="modal" data-bs-target="#modalMinus">
                            <i class="fas fa-minus"></i>
                        </button>
                    </td>
                    <td>
                        <button class="btn btn-outline-secondary" onclick="showFormTransfer(${item.id})" data-bs-toggle="modal" data-bs-target="#modalTransfer">
                            <i class="fas fa-exchange-alt"></i>
                        </button>
                    </td>
                    <td>
                        <button class="btn btn-outline-danger" onclick="deleteById(${item.id})">
                            <i class="fas fa-user-slash"></i>
                        </button>
                    </td>
                </tr>
            `;
}

// const renderFooter = (item) => {
//     return `
//                 <div class="container">
                    
                    
//                         <button class="btn btn-outline-primary edit" data-id="${item.id}" data-bs-toggle="modal" data-bs-target="#modalUpdate">
//                             <i class="fas fa-user-edit"></i>
//                         </button>
                    
                    
//                         <button class="btn btn-outline-success" onclick="showPlus(${item.id})" data-bs-toggle="modal" data-bs-target="#modalPlus">
//                             <i class="fas fa-plus"></i>
//                         </button>
                    
                    
//                         <button class="btn btn-outline-warning" onclick="showMinus(${item.id})" data-bs-toggle="modal" data-bs-target="#modalMinus">
//                             <i class="fas fa-minus"></i>
//                         </button>
                    
                    
//                         <button class="btn btn-outline-secondary" onclick="showFormTransfer(${item.id})" data-bs-toggle="modal" data-bs-target="#modalTransfer">
//                             <i class="fas fa-exchange-alt"></i>
//                         </button>
                   
//                         <button class="btn btn-outline-danger" onclick="deleteById(${item.id})">
//                             <i class="fas fa-user-slash"></i>
//                         </button>
//                     </td>
//                 </div>
//             `;

// }

const strBody = $('#tbCustomerBody');
const getAllCustomers = () => {
    // dùng ajax để call api, dùng Jquery để action HTTP đến 1 url cụ thể
    $.ajax({
        type: 'get',
        url: `http://localhost:3300/customers?page=${page || 0}&size=${size || 0}`,
        success: function (res) {
            strBody.empty(); // clear hết element có id là strBody, clear dữ liệu cũ trước khi hiển thị dữ liệu mới
            totalPage = res.totalPages;

            // lặp qua mảng content trong obj res
            $.each(res, (index, item) => {
                const str = renderCustomer(item); //gọi hàm tạo chuỗi HTML dựa trên dữ liệu khách hàng đc truyền vào

                strBody.append(str); //thêm chuỗi HTML str vào đầu pt id strBody, hiển thị khách hàng

            })


            renderPagination();

            // gán 1 sự kiện click cho tất cả các pt có class CSS là edit.
            $('.edit').on('click', function () {
                const id = $(this).data('id');

                $.ajax({
                    headers: {
                        'accept': 'application/json',
                        'content-type': 'application/json'
                    },
                    method: "GET",
                    url: url + '/' + id,
                })
                    .done((data) => {
                        if (Object.keys(data).length > 0) {
                            let customer = data;
                            

                            $('#idUp').val(customer.id);
                            $('#fullNameUp').val(customer.fullName);
                            $('#emailUp').val(customer.email);
                            $('#phoneUp').val(customer.phone);

;

                            $('#modalUpdate').show();
                        }
                        else {
                            alert('Say No');
                        }
                    })
                    .fail((error) => {
                        console.log(error);
                    })

            })

        }, error: function () {
            alert('Error');
        }
    });

}

getAllCustomers();

const btnCreate = $('#btnCreate');
// const btnCreate = document.getElementById('btnCreate')
btnCreate.on('click', () => {
    btnCreate.attr('disabled', true);
    let load = webToast.loading({
        status: 'Loading...',
        message: 'Please Wait a moment',
        align: 'bottomright',
        delay: 1000,
        line: true,
    });

    const inputFields = getDataInput();
    let hasError = false;
    // kiểm tra và hiển thị thông báo lỗi cho mỗi trường
    inputFields.forEach((field) => {
        const inputElement = $(`#${field.name}Cre`);
        const value = inputElement.val();
        const errorContainer = $(`#${field.name}ErrorContainer`); // thêm container cho thông báo lỗi

        if (field.required && !value) {
            hasError = true;
            errorContainer.text(`Please enter a valid ${field.label}`); // hiển thị thông báo lỗi
        } else if (field.pattern && !new RegExp(field.pattern).test(value)) {
            hasError = true;
            errorContainer.text(field.message); // hiển thị thông báo lỗi
        } else {
            errorContainer.text(''); // xóa thông báo lỗi nếu hợp lệ
        }
    });

    if (hasError) {
        // nếu có lỗi, không thực hiện yêu cầu AJAX và kết thúc
        btnCreate.attr('disabled', false);
        load.remove();
        return;
    }

    const obj = {
        fullName: $('#fullNameCre').val(),
        email: $('#emailCre').val(),
        phone: $('#phoneCre').val(),
    
        balance: 0,
        deleted: 0
    }

    setTimeout(() => {
        $.ajax({
            headers: {
                'accept': 'application/json',
                'content-type': 'application/json'
            },
            type: 'POST',
            url: 'http://localhost:3300/customers',
            dataType: 'json',
            data: JSON.stringify(obj),
        })
            .done((data) => {
                console.log("Success");
                const str = renderCustomer(data);
                getAllCustomers();
                $(strBody).prepend(str);


                $('#modalCreate').hide();
                $('#closeCre').click();

                webToast.Success({
                    status: 'Thêm mới thành công',
                    message: '',
                    delay: 2000,
                    align: 'topright'
                });
                $('#formCre').trigger("reset");

            })
            .fail((error) => {
                console.log(error);
            })
            .always(() => {
                btnCreate.attr('disabled', false);
                load.remove();


            })
    }, 5000);
})

const btnUpdate = $('#btnUpdate');
btnUpdate.on('click', () => {
    btnUpdate.attr('disabled', true);
    const customerId = $('#idUp').val();


    let load = webToast.loading({
        status: 'Loading...',
        message: 'Please Wait a moment',
        align: 'bottomright',
        delay: 1000,
        line: true,
    });

    const inputFields = getDataInput();
    let hasError = false;
    inputFields.forEach((field) => {
        const inputElement = $(`#${field.name}Up`);
        const value = inputElement.val();
        const error = $(`#${field.name}Error`); // thêm container cho thông báo lỗi

        if (field.required && !value) {
            hasError = true;
            error.text(`Please enter a valid ${field.label}`); // hiển thị thông báo lỗi
        } else if (field.pattern && !new RegExp(field.pattern).test(value)) {
            hasError = true;
            error.text(field.message); // hiển thị thông báo lỗi
        } else {
            error.text(''); // xóa thông báo lỗi nếu hợp lệ
        }
    });

    if (hasError) {
        // nếu có lỗi, không thực hiện yêu cầu AJAX và kết thúc
        btnUpdate.attr('disabled', false);
        load.remove();
        return;
    }

    const obj = {
        fullName: $('#fullNameUp').val(),
        email: $('#emailUp').val(),
        phone: $('#phoneUp').val(),

    }

    setTimeout(() => {
        $.ajax({
            headers: {
                'accept': 'application/json',
                'content-type': 'application/json'
            },
            type: 'PATCH',
            url: 'http://localhost:3300/customers/' + customerId,
            data: JSON.stringify(obj),
        })
            .done((data) => {
                const str = renderCustomer(data);
                const currentRow = $('#tr_' + customerId);
                currentRow.replaceWith(str);

                $('#modalUpdate').hide();
                $('#closeUp').click();


                webToast.Success({
                    status: 'Cập nhật thành công',
                    message: '',
                    delay: 2000,
                    align: 'topright'
                });


            })
            .fail((error) => {
                console.log(error);
            })
            .always(() => {
                btnUpdate.attr('disabled', false);
                load.remove();
            })
    }, 5000);
})

function deleteById(customerId) {
    const balance = +$("#balance-" + customerId).text();
    if (balance > 0) {
        webToast.Danger({
            status: 'Cảnh báo',
            message: 'Không thể xóa người dùng có số dư lớn hơn 0',
            delay: 2000,
            align: 'topright'
        });
    } else {
        const confirmBox = webToast.confirm("Are you sure to delete Customer " + customerId + "?")
        confirmBox.click(() => {
            $.ajax({
                headers: {
                    'accept': 'application/json',
                    'content-type': 'application/json'
                },
                url: 'http://localhost:3300/customers/' + customerId,
                method: 'DELETE',
            }).done(e => {
                webToast.Success({
                    status: 'Xóa thành công',
                    message: '',
                    delay: 2000,
                    align: 'topright'
                });
                getAllCustomers()
            })
        })

    }
}


function showPlus(id) {
    $.ajax({
        url: url + "/" + id,
        method: "GET"
    }).done(data => {
        $("#fullNamePlus").val(data.fullName);
        $("#emailPlus").val(data.email);
        $("#balancePlus").val(data.balance)
        document.getElementById("btnPlus").onclick = function () {
            plus(data.id)
        }
    })
}

function plus(id) {
    const amount = +$("#deposit").val();
    // const balance = +$("#balance-" + id).text();
    const balanceElement = $("#balance-" + id);

    if (amount <= 0) {
        webToast.Danger({
            status: 'Cảnh báo',
            message: 'Số tiền nhập vào phải lớn hơn 0',
            delay: 2000,
            align: 'topright'
        });
        $("#deposit").val("");
        return;
    }


    let deposit = {
        idCustomer: id,
        deposit: amount
    }

    $.ajax({
        headers: {
            'accept': 'application/json',
            'content-type': 'application/json'
        },
        url: "http://localhost:3300/deposits/",
        method: 'POST',
        dataType: 'json',
        data: JSON.stringify(deposit)
    }).done(data => {

        $.ajax({
            headers: {
                'accept': 'application/json',
                'content-type': 'application/json'
            },
            url: "http://localhost:3300/customers/" + id,
            method: 'GET',
        })
            .done((data) => {
                const currentBalance = data.balance
                const newBalance = currentBalance + amount

                let customer = {
                    balance: newBalance
                }

                $.ajax({
                    headers: {
                        'accept': 'application/json',
                        'content-type': 'application/json'
                    },
                    url: "http://localhost:3300/customers/" + id,
                    method: 'PATCH',
                    data: JSON.stringify(customer)
                })
                    .done((data) => {
                        const str = renderCustomer(data)
                        $('#tr_' + id).replaceWith(str)

                        $('#modalPlus').hide();
                        $('#closePlus').click();

                        $("#deposit").val("");

                        webToast.Success({
                            status: 'Nạp tiền thành công !!!',
                            message: 'Số dư tài khoản: ' + newBalance,
                            // message: 'Số dư tài khoản: ' + (amount + balance),
                            delay: 2000,
                            align: 'topright'
                        });
                    })
            })
    })
}
function showMinus(id) {

    $.ajax({
        url: url + "/" + id,
        method: "GET"
    }).done(data => {
        $("#fullNameMinus").val(data.fullName);
        $("#emailMinus").val(data.email);
        $("#balanceMinus").val(data.balance);
        document.getElementById("btnMinus").onclick = function () {
            minus(data.id)
        }
    })
}
function minus(id) {
    const amount = +$("#withdraw").val();
    const balanceElement = $("#balance-" + id);
    const balance = parseFloat(balanceElement.text());

    if (isNaN(amount) || amount <= 0) {
        webToast.Danger({
            status: 'Cảnh báo',
            message: 'Số tiền rút ra phải lớn hơn 0',
            delay: 2000,
            align: 'topright'
        });
        $("#withdraw").val("");
        return;
    }

    if (amount > balance) {
        webToast.Danger({
            status: 'Cảnh báo',
            message: 'Số tiền dư không đủ để rút !!',
            delay: 2000,
            align: 'topright'
        });
        $("#withdraw").val("");
    } else {
        const withdraw = {
            idCustomer: id,
            withdraw: amount,
            balance: balance
        };

        $.ajax({
            headers: {
                'accept': 'application/json',
                'content-type': 'application/json'
            },
            url: "http://localhost:3300/withdraws/",
            method: 'POST',
            dataType: 'json',
            data: JSON.stringify(withdraw)
        })
            .done(data => {
                $.ajax({
                    headers: {
                        'accept': 'application/json',
                        'content-type': 'application/json'
                    },
                    url: "http://localhost:3300/customers/" + id,
                    method: 'GET',
                })
                    .done(data => {
                        const currentBalance = data.balance;
                        const newBalance = currentBalance - amount;
                        const customer = {
                            balance: newBalance
                        };

                        $.ajax({
                            headers: {
                                'accept': 'application/json',
                                'content-type': 'application/json'
                            },
                            url: "http://localhost:3300/customers/" + id,
                            method: 'PATCH',
                            data: JSON.stringify(customer)
                        })
                            .done(data => {
                                const str = renderCustomer(data);
                                $('#tr_' + id).replaceWith(str);

                                $('#modalMinus').hide();

                                $('#closeMinus').click();

                                $("#withdraw").val("");

                                webToast.Success({
                                    status: 'Rút tiền thành công !!!',
                                    message: 'Số dư tài khoản: ' + newBalance,
                                    delay: 2000,
                                    align: 'topright'
                                });
                            });
                    });
            });
    }
}

function showFormTransfer(id) {
    document.getElementById("balance").value = document.getElementById("balance-" + id).innerText;
    document.getElementById("fullNameSender").value = document.getElementById("tr_" + id).querySelectorAll("td")[1].innerText;
    document.getElementById("emailSender").value = document.getElementById("tr_" + id).querySelectorAll("td")[2].innerText;

    document.getElementById("selectTransfer").addEventListener("change", function () {
        const selectedOption = this.options[this.selectedIndex];
        document.getElementById("emailRecipient").value = selectedOption.getAttribute("data-email");
    });

    $.ajax({
        url: url,
        method: "GET"
    }).done(data => {
        const transferCustomer = data.filter(customer => customer.id !== id);
        let html = '<option>--Chọn người cần chuyển khoản--</option>';
        transferCustomer.map(customer => {
            html += `<option value="${customer.id}" data-email="${customer.email}">${customer.fullName}</option>`;
        })
        document.getElementById("selectTransfer").innerHTML = html;
        document.getElementById("btnTransfer").onclick = function () {
            const senderId = document.getElementById("tr_" + id).querySelectorAll("td")[0].innerText;
            const recipientId = document.getElementById("selectTransfer").value;
            transferAmount(senderId, recipientId)
        }
    });
}
function transferAmount(idSender, idRecipient) {
    const amount = +$("#transfer").val();
    const balanceSender = +$("#balance-" + idSender).text();
    const balanceRecipient = +$("#balance-" + idRecipient).text();
    const name = document.getElementById("selectTransfer").selectedOptions[0].text;

    if (isNaN(amount) || amount <= 0) {
        webToast.Danger({
            status: 'Cảnh báo',
            message: 'Số tiền chuyển khoản phải lớn hơn 0!!',
            delay: 2000,
            align: 'topright'
        });
        $("#transfer").val("");
        return;
    }

    if (confirm("Bạn chắc chắn muốn chuyển " + amount + " cho " + name + "???")) {
        if (amount > balanceSender) {
            webToast.Danger({
                status: 'Cảnh báo',
                message: 'Số tiền dư không đủ để chuyển khoản !!!',
                delay: 2000,
                align: 'topright'
            });
            $("#transfer").val("");
        } else if (amount > 0) {
            const transfer = {
                idCustomer: idSender, idRecipient,
                senderId: idSender,
                recipientId: idRecipient,
                transactionAmount: amount,
            };

            // trừ tiền từ tài khoản người gửi
            const newBalanceSender = balanceSender - amount;
            const senderUpdate = {
                balance: newBalanceSender,
            };

            // cộng tiền vào tài khoản người nhận
            const newBalanceRecipient = balanceRecipient + amount;
            const recipientUpdate = {
                balance: newBalanceRecipient,
            };

            // thực hiện giao dịch và cập nhật tài khoản
            $.ajax({
                headers: {
                    'accept': 'application/json',
                    'content-type': 'application/json'
                },
                url: "http://localhost:3300/transfers/",
                method: 'POST',
                dataType: 'json',
                data: JSON.stringify(transfer)
            }).done(data => {
                // cập nhật tài khoản người gửi
                $.ajax({
                    headers: {
                        'accept': 'application/json',
                        'content-type': 'application/json'
                    },
                    url: "http://localhost:3300/customers/" + idSender,
                    method: 'PATCH',
                    data: JSON.stringify(senderUpdate)
                }).done(data => {
                    const str = renderCustomer(data);
                    $('#tr_' + idSender).replaceWith(str);
                });

                // cập nhật tài khoản người nhận
                $.ajax({
                    headers: {
                        'accept': 'application/json',
                        'content-type': 'application/json'
                    },
                    url: "http://localhost:3300/customers/" + idRecipient,
                    method: 'PATCH',
                    data: JSON.stringify(recipientUpdate)
                }).done(data => {
                    const str = renderCustomer(data);
                    $('#tr_' + idRecipient).replaceWith(str);

                    $('#modalTransfer').hide();
                    $('#closeTransfer').click();

                    $("#transfer").val("");

                    webToast.Success({
                        status: 'Chuyển tiền thành công !!!',
                        message: 'Số dư tài khoản người gửi: ' + newBalanceSender,
                        delay: 2000,
                        align: 'topright'
                    });
                });
            });
        } else {
            $('#modalTransfer').hide();
            $('#closeTransfer').click();
            $('#formTransfer').trigger("reset");
        }
    }
}


const renderPagination = () => {
    const pagination = $('#pagination');
    pagination.empty();
    pagination.append(` <li onclick="onPageChange(${page})"
        class="page-item ${page === 0 ? 'disabled' : ''}">
      <a class="page-link" href="#" tabindex="-1" ${page === 0 ? 'aria-disabled="true"' : ''} ><span aria-hidden="true">&laquo;</span></a>
    </li>`)
    for (let i = 1; i <= totalPage; i++) {
        pagination
            .append(`<li class="page-item" onclick="onPageChange(${i})">
                            <a class="page-link ${page + 1 === i ? 'active' : ''} "
                            ${page + 1 === i ? 'aria-current="page"' : ''} href="#">
                                ${i}
                            </a></li>`);

    }

    pagination.append(` <li onclick="onPageChange(${page + 2})"
        class="page-item ${page === totalPage - 1 ? 'disabled' : ''}">
      <a class="page-link" href="#" tabindex="-1" ${page === totalPage - 1 ? 'aria-disabled="true"' : ''} ><span aria-hidden="true">&raquo;</span></a>
    </li>`);
}
const onPageChange = (pageChange) => {
    if (pageChange < 1 || pageChange > totalPage || pageChange === page + 1) {
        return;
    }
    page = pageChange - 1;
    getAllCustomers();
}


function getDataInput() {
    return [
        {
            label: 'Full Name',
            classContainer: "col-6 mt-3",
            name: 'fullName',
            value: bankingSelected.fullName,
            required: true,
            pattern: "^[A-Za-z]{4,15}",
            message: "Full Name must have minimum is 4 characters and maximum is 15 characters",
        },

        {
            label: 'Phone',
            classContainer: "col-12 mt-3",
            name: 'phone',
            value: bankingSelected.phone,
            pattern: /^\+\d\s\(\d{3}\)\s\d{3}-\d{4}$/,
            message: "Phone is between +X (XXX) XXX-XXXX",
            required: true
        },
        {
            label: 'Email',
            classContainer: "col-12 mt-3",
            name: 'email',
            value: bankingSelected.email,
            pattern: /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/,
            message: "Please enter a valid email address",
            required: true
        }
    ];
}